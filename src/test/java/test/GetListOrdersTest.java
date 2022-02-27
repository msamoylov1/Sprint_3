package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOrdersTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders - получить список заказов")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getListOrdersTest() {

        given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders")
                .then().log().all().statusCode(200)
                .and().body("orders", notNullValue())
                .and().body("orders.id", notNullValue())
                .and().body("orders.firstName", notNullValue())
                .and().body("orders.lastName", notNullValue())
                .and().body("orders.address", notNullValue())
                .and().body("orders.metroStation", notNullValue())
                .and().body("orders.phone", notNullValue())
                .and().body("orders.rentTime", notNullValue())
                .and().body("orders.deliveryDate", notNullValue())
                .and().body("orders.track", notNullValue());
    }

}
