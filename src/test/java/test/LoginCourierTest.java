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

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest extends DataHalper {

    //генерация рандомного login для курьера
    String randomLogin = getRandomString(5);

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
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

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (успех)")
    @Description("Отправляются все 2 поля - корректно заполненные")
    public void trueLoginCourierTest() {
        //логин за ранее созданного курьера
        getLoginCourier();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (404 - несуществующий логин)")
    @Description("Отправляются все 2 поля - несуществующий логин, ожидаем ошибку 404")
    public void loginFalseLoginCourierTest() {
        getFalseLoginCourier();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (404 - несуществующий пароль)")
    @Description("Отправляются все 2 поля - несуществующий пароль, ожидаем ошибку 404")
    public void loginFalsePassCourierTest() {
        getFalsePassCourier();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (400 - пустое/нет поля логин)")
    @Description("Отправляются 1 поле(password)/2 поля, но в login пусто - ожидаем ошибку 400")
    public void loginNoLoginCourierTest() {
        //кейс 1 - пустое поле логин
        getNullLoginCourier();
        //кейс 2 - нет поля логин
        getNoLoginCourier();
    }

    //1 из тестов не проходит (когда не отправляется поле password - это баг)
    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (400 - пустое/нет поля пароль)")
    @Description("Отправляются 1 поле(login)/2 поля, но в password пусто - ожидаем ошибку 400")
    public void loginNoPassCourierTest() {
        //кейс 1 - пустое поле логин
        getNullPassCourier();
        //кейс 2 - нет поля логин
        getNoPassCourier();
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
                        response.then().log().all().statusCode(200)
                        .and().body("id", notNullValue());
        JsonPath jsonPathOrder = response.jsonPath();
        idCourier = jsonPathOrder.get("id");
    }

    @Step("Логинимся за созданного курьера - несуществующий логин")
    public void getFalseLoginCourier() {
        String json = "{\"login\": \"t+00+00\", \"password\": \"123\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
                        response.then().log().all().statusCode(404)
                        .and().body("message", equalTo("Учетная запись не найдена"));
    }
    @Step("Логинимся за созданного курьера - несуществующий пароль")
    public void getFalsePassCourier() {
        String json = "{\"login\": \""  + randomLogin + "\", \"password\": \"0000\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Step("Логинимся за созданного курьера - несуществующий логин")
    public void getNullLoginCourier() {
        String json = "{\"login\": \"\", \"password\": \"123\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Step("Логинимся за созданного курьера - несуществующий логин")
    public void getNoLoginCourier() {
        String json = "{\"password\": \"123\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для входа"));
    }


    @Step("Логинимся за созданного курьера - несуществующий пароль")
    public void getNullPassCourier() {
        String json = "{\"login\": \""  + randomLogin + "\", \"password\": \"\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для входа"));
    }
    @Step("Логинимся за созданного курьера - несуществующий пароль")
    public void getNoPassCourier() {
        String json = "{\"login\": \""  + randomLogin + "\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier/login");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void deleteCourier() {
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
        //удаление курьера
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier);

    }
}
