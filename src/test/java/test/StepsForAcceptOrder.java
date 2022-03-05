package test;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class StepsForAcceptOrder {

    @Step("Создаём нового курьера")
    public Response sendNewCourier() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, "123", "max1");
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(createCourier)
                .when()
                .post("/api/v1/courier");
        return response;
    }
    @Step("Создаём новый заказ и записываем track")
    public void sendPostOrder() {
        CreateOrder createOrder = new CreateOrder(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                List.of("BLACK"));
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createOrder)
                        .when()
                        .post("/api/v1/orders");
        response.then().log().all().statusCode(201)
                .and().body("track", notNullValue());

        JsonPath jsonPathOrder = response.jsonPath();
        DataHelper.track = jsonPathOrder.get("track");
    }
    @Step("Получаем заказ по трек-номеру")
    public Response getOrderByTrack() {

        Response response =  given().log().all()
                .header("Content-type", "application/json")
                .when()
                .param("t", DataHelper.track)
                .get("/api/v1/orders/track");

        JsonPath jsonPathOrder = response.jsonPath();
        DataHelper.idOrder = jsonPathOrder.get("order.id");
        return response;
    }
    @Step("Логинимся за созданного курьера")
    public void loginCourierAndGetId() {
        LoginCourier loginCourier = new LoginCourier(DataHelper.randomLogin, "123");
        ResponseLoginCourier responseLoginCourier =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login")
                        .as(ResponseLoginCourier.class);
        DataHelper.idCourier = responseLoginCourier.getId();
    }

    @Step("Принимаем ранее созданный заказ")
    public Response acceptNewOrder() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", DataHelper.idCourier)
                .put("/api/v1/orders/accept/" + DataHelper.idOrder);
        return response;
    }

    @Step("Принимаем ранее созданный заказ - нет id курьера")
    public Response acceptNoIdCourierNewOrder() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .put("/api/v1/orders/accept/" + DataHelper.idOrder);
        return response;
    }

    @Step("Принимаем ранее созданный заказ - нет id заказа")
    public Response acceptNoIdOrderNewOrder() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", DataHelper.idCourier)
                .put("/api/v1/orders/accept/");
        return response;
    }

    @Step("Принимаем ранее созданный заказ - несуществующий id заказа")
    public Response acceptFalseIdOrderNewOrder() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", DataHelper.idCourier)
                .put("/api/v1/orders/accept/" + "0000");
        return response;
    }

    @Step("Принимаем ранее созданный заказ - несуществующий id курьера")
    public Response acceptFalseIdCourierNewOrder() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", "0000")
                .put("/api/v1/orders/accept/" + DataHelper.idOrder);
        return response;
    }

    @Step("Завершаем заказ")
    public void finishOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .put("/api/v1/orders/finish/" + DataHelper.idOrder);
    }
    @Step("Отменяем заказ")
    public void cancelOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("track", DataHelper.track)
                .put("/api/v1/orders/cancel");
    }
    @Step("Удалим ранее созданного курьера")
    public Response deleteNewCourier() {
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .when()
                        .delete("/api/v1/courier/" + DataHelper.idCourier);
        return response;
    }
}
