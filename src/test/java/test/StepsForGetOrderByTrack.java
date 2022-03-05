package test;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class StepsForGetOrderByTrack {

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

    @Step("Получаем заказ по трек-номеру - запрос без номера")
    public Response getOrderByNullTrack() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders/track");
        return response;
    }

    @Step("Получаем заказ по трек-номеру - несуществующий номер")
    public Response getOrderByFalseTrack() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .when()
                .param("t", "00000")
                .get("/api/v1/orders/track");
        return response;
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
}
