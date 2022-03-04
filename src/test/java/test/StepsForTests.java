package test;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class StepsForTests {

    public void baseUrl() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
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

    @Step("Получаем список заказов")
    public Response getListOrders() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        return response;
    }

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

    @Step("Логинимся за созданного курьера")
    public Response loginCourier() {
        LoginCourier loginCourier = new LoginCourier(DataHelper.randomLogin, "123");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
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

    //запросы с ошибками
    @Step("Логинимся за созданного курьера - несуществующий логин")
    public Response getFalseLoginCourier() {
        LoginCourier loginCourier = new LoginCourier("t+00+00", "123");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }
    @Step("Логинимся за созданного курьера - несуществующий пароль")
    public Response getFalsePassCourier() {
        LoginCourier loginCourier = new LoginCourier(DataHelper.randomLogin, "00+00");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step("Логинимся за созданного курьера - пустое поле login")
    public Response getNullLoginCourier() {
        LoginCourier loginCourier = new LoginCourier(null, "123");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }
    @Step("Логинимся за созданного курьера - пробел в поле login")
    public Response getNoLoginCourier() {
        LoginCourier loginCourier = new LoginCourier(" ", "123");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
        return response;
    }

    @Step("Логинимся за созданного курьера - пробел в поле пароль")
    public Response getNoPassCourier() {
        LoginCourier loginCourier = new LoginCourier(DataHelper.randomLogin, " ");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourier)
                        .when()
                        .post("/api/v1/courier/login");
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

    @Step("Удалим ранее созданного курьера - не отправляем id курьера")
    public Response deleteNoIdNewCourier() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/");
        return response;
    }

    @Step("Удалим ранее созданного курьера - не отправляем id курьера")
    public Response deleteFalseIdNewCourier() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + "0000");
        return response;
    }

    @Step("Создание курьера с уже использованным login")
    public Response sendNewDoubleCourier() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, "123", "max1");
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Создание курьера - пустое поле login")
    public Response sendNewCourierNullLogin() {
        CreateCourier createCourier = new CreateCourier(null, "123", "max1");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }
    @Step("Создание курьера - пробел в поле login")
    public Response sendNewCourierNoLogin() {
        CreateCourier createCourier = new CreateCourier(" ", "123", "max1");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Создание курьера - пустое поле password")
    public Response sendNewCourierNullPassword() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, null, "max1");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }
    @Step("Создание курьера - пробел в поле password")
    public Response sendNewCourierNoPassword() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, " ", "max1");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }

    @Step("Создание курьера - пустое поле firstName")
    public Response sendNewCourierNullFirstname() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, "123", null);
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }
    @Step("Создание курьера - пробел в поле firstName")
    public Response sendNewCourierNoFirstname() {
        CreateCourier createCourier = new CreateCourier(DataHelper.randomLogin, "123", " ");
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createCourier)
                        .when()
                        .post("/api/v1/courier");
        return response;
    }


}
