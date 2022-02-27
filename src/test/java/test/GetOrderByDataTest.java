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

public class GetOrderByDataTest extends DataHalper {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (успех)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByTrackTest() {

        //создаём заказ
        sendPostOrder();

        //получаем заказ по трек-номеру
        getOrderByTrack();
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (400 - запрос без номера)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByNullTrackTest() {

        //создаём заказ
        sendPostOrder();

        //получаем заказ по трек-номеру - запрос без номера
        getOrderByNullTrack();
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (404 - несуществующий номер)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByFalseTrackTest() {

        //создаём заказ
        sendPostOrder();

        //получаем заказ по трек-номеру - несуществующий номер
        getOrderByFalseTrack();
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
        track = jsonPathOrder.get("track");
   }

   @Step("Получаем заказ по трек-номеру")
   public void getOrderByTrack() {

       given().log().all()
               .header("Content-type", "application/json")
               .when()
               .param("t", track)
               .get("/api/v1/orders/track")
               .then().log().all().statusCode(200)
               .and().body("order", notNullValue())
               .and().body("order.id", notNullValue())
               .and().body("order.firstName", notNullValue())
               .and().body("order.lastName", notNullValue())
               .and().body("order.address", notNullValue())
               .and().body("order.metroStation", notNullValue())
               .and().body("order.phone", notNullValue())
               .and().body("order.rentTime", notNullValue())
               .and().body("order.deliveryDate", notNullValue())
               .and().body("order.track", equalTo(track));
   }

   @Step("Получаем заказ по трек-номеру - запрос без номера")
   public void getOrderByNullTrack() {

        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders/track")
                .then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

   @Step("Получаем заказ по трек-номеру - несуществующий номер")
   public void getOrderByFalseTrack() {

        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .param("t", "00000")
                .get("/api/v1/orders/track")
                .then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказ не найден"));
    }

    @After
    public void cancelOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("track", track)
                .put("/api/v1/orders/cancel");
    }
}
