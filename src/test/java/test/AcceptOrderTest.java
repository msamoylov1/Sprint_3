package test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest extends DataHalper {

    //генерация рандомного login для курьера
    String randomLogin = getRandomString(5);

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (успех)")
    @Description("Все передаваемые данные (id курьера/заказа) - валидные")
    public void acceptOrderFullFieldTest() {

        //создаём заказ и получаем трек-номер заказа
        Response response = sendPostOrder();

        //получаем заказ по номеру и записываем его id
        getOrderByTrack();

        //создаём курьера
        sendNewCourier();

        //логинимся курьером и получаем id
        getLoginCourier();

        //принимаем заказ
        acceptNewOrder();

        //завершаем заказ
        finishOrder();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id курьера)")
    @Description("id курьера не передаётся в запросе")
    public void acceptOrderNoIdCourierTest() {

        //создаём заказ и получаем трек-номер заказа
        Response response = sendPostOrder();

        //получаем заказ по номеру и записываем его id
        getOrderByTrack();

        //создаём курьера
        sendNewCourier();

        //логинимся курьером и получаем id
        getLoginCourier();

        //принимаем заказ
        acceptNoIdCourierNewOrder();

        //отменим заказ - очистка данных
        cancelOrder();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id заказа)")
    @Description("id заказа не передаётся в запросе")
    public void acceptOrderNoIdCOrderTest() {

        //создаём заказ и получаем трек-номер заказа
        Response response = sendPostOrder();

        //получаем заказ по номеру и записываем его id
        getOrderByTrack();

        //создаём курьера
        sendNewCourier();

        //логинимся курьером и получаем id
        getLoginCourier();

        //принимаем заказ
        acceptNoIdOrderNewOrder();

        //отменим заказ - очистка данных
        cancelOrder();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id заказа)")
    @Description("Передаётся несуществующий id заказа в запросе")
    public void acceptOrderFalseIdOrderTest() {

        //создаём заказ и получаем трек-номер заказа
        Response response = sendPostOrder();

        //получаем заказ по номеру и записываем его id
        getOrderByTrack();

        //создаём курьера
        sendNewCourier();

        //логинимся курьером и получаем id
        getLoginCourier();

        //принимаем заказ
        acceptFalseIdOrderNewOrder();

        //отменим заказ - очистка данных
        cancelOrder();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id курьера)")
    @Description("Передаётся несуществующий id курьера в запросе")
    public void acceptOrderFalseIdCourierTest() {

        //создаём заказ и получаем трек-номер заказа
        Response response = sendPostOrder();

        //получаем заказ по номеру и записываем его id
        getOrderByTrack();

        //создаём курьера
        sendNewCourier();

        //логинимся курьером и получаем id
        getLoginCourier();

        //принимаем заказ
        acceptFalseIdCourierNewOrder();

        //отменим заказ - очистка данных
        cancelOrder();
    }

    @Step("Создаём новый заказ и записываем track")
    public Response sendPostOrder() {
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
        track = jsonPathOrder.get("track");
        return response;
    }

    @Step("Получаем заказ по трек-номеру")
    public void getOrderByTrack() {

        Response response =  given().log().all()
                .header("Content-type", "application/json")
                .when()
                .param("t", track)
                .get("/api/v1/orders/track");

        JsonPath jsonPathOrder = response.jsonPath();
        idOrder = jsonPathOrder.get("order.id");
    }

    @Step("Создаём нового курьера")
    public void sendNewCourier() {
        String json = "{\"login\": \"" + randomLogin + "\", \"password\": \"123\", \"firstName\": \"max1\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        response.then().log().all().statusCode(201)
                        .and().body("ok", equalTo(true));
    }

    @Step("Логинимся за созданного курьера")
    public void getLoginCourier() {
        String json = "{\"login\": \""  + randomLogin + "\", \"password\": \"123\"}";
        Response response =
                        given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        JsonPath jsonPathOrder = response.jsonPath();
        idCourier = jsonPathOrder.get("id");
    }

    @Step("Принимаем ранее созданный заказ")
    public void acceptNewOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", idCourier)
                .put("/api/v1/orders/accept/" + idOrder)
                .then().log().all().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    @Step("Принимаем ранее созданный заказ - нет id курьера")
    public void acceptNoIdCourierNewOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .put("/api/v1/orders/accept/" + idOrder)
                .then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Step("Принимаем ранее созданный заказ - нет id заказа")
    public void acceptNoIdOrderNewOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", idCourier)
                .put("/api/v1/orders/accept/")
                .then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Step("Принимаем ранее созданный заказ - несуществующий id заказа")
    public void acceptFalseIdOrderNewOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", idCourier)
                .put("/api/v1/orders/accept/" + "0000")
                .then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказа с таким id не существует"));
    }

    @Step("Принимаем ранее созданный заказ - несуществующий id заказа")
    public void acceptFalseIdCourierNewOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("courierId", "0000")
                .put("/api/v1/orders/accept/" + idOrder)
                .then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id не существует"));
    }

    @Step("Завершаем заказ")
    public void finishOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .put("/api/v1/orders/finish/" + idOrder);
    }

    @Step("Отменяем заказ")
    public void cancelOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("track", track)
                .put("/api/v1/orders/cancel");
    }

    @After
    public void deleteCourier() {
        //удаление курьера
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier)
                .then().log().all().statusCode(200)
                .and().body("ok", equalTo(true));

    }
}
