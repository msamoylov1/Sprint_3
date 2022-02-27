package test;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest extends DataHalper {

    //генерация рандомного login для курьера
    String randomLogin = getRandomString(5);

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (успех)")
    @Description("Отправляются существующий id курьера")
    public void deleteCourierTest() {
        //создаём нового курьера
        Response response = sendNewCourier();

        //логинимся за ранее созданног курьера и записываем его id
        getLoginCourier();

        //удаляем ранее созданного курьера
        deleteNewCourier();
    }

    //баг - в тексте сообщения об ошибке "Курьера с таким id нет." - лишняя точка
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 404)")
    @Description("Отправляются несуществующий id курьера")
    public void deleteCourierFalseIdTest() {

        //создаём нового курьера
        Response response = sendNewCourier();

        //логинимся за ранее созданног курьера и записываем его id
        getLoginCourier();

        //удаление курьера
        deleteFalseIdNewCourier();

        //почистим базу и удалим созданного курьера
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }

    //баг - в тексте сообщения об ошибке "Not Found.", ошибка 404 (должна быть 400)
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 400)")
    @Description("Не отправляются id курьера")
    public void deleteCourierNoIdTest() {

        //создаём нового курьера
        Response response = sendNewCourier();

        //логинимся за ранее созданного курьера и записываем его id
        getLoginCourier();

        //удаление курьера
        deleteNoIdNewCourier();

        //почистим базу и удалим созданного курьера
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }


    @Step("Создаём нового курьера")
    public Response sendNewCourier() {
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
        return response;
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

    @Step("Удалим ранее созданного курьера")
    public void deleteNewCourier() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier)
                .then().log().all().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    @Step("Удалим ранее созданного курьера - не отправляем id курьера")
    public void deleteNoIdNewCourier() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/")
                .then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @Step("Удалим ранее созданного курьера - не отправляем id курьера")
    public void deleteFalseIdNewCourier() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + "0000")
                .then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id нет"));
    }

}
