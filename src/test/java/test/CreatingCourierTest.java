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

public class CreatingCourierTest extends DataHalper {

    //генерация рандомного login для курьера
    String randomLogin = getRandomString(5);

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (успех)")
    @Description("Отправляются все 3 поля - корректно заполненные")
    public void createCourierWellCaseTest() {
        //успешно создаём курьера
        sendNewCourier();

        //чистим базу
        deleteData();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера дублирование login")
    @Description("Попытка создать курьера с уже использующимся логином - ошибка 409")
    public void createTwoCourierOneLoginTest() {

        //пытаемся создать нового курьера с таким же логином
        sendNewDoubleCourier();

        //требуется чистить базу ибо создали одного курьера
        deleteData();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера")
    @Description("Отправляются 2 поля(password/firstName)/3 поля, но в login пусто - ошибка 400")
    public void createCourierNoLoginTest() {
        //кейс 1 - пустое поле логин
        sendNewCourierNullLogin();
        //кейс 2 - нет поля логин
        sendNewCourierNoLogin();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера")
    @Description("Отправляются 2 поля(login/firstName)/3 поля, но в password пусто - ошибка 400")
    public void createCourierNoPasswordTest() {
        //кейс 1 - пустое поле пароль
        sendNewCourierNullPassword();
        //кейс 2 - нет поля пароль
        sendNewCourierNoPassword();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера")
    @Description("Отправляются 2 поля(login/password)/3 поля, но в firstName пусто - ошибка 400")
    //в этом тесте есть баг - firstName необязательное при создании курьера
    public void createCourierNoFirstNameTest() {
        //кейс 1 - пустое поле фамилия
        sendNewCourierNullFirstname();
        //кейс 2 - нет поля фамилия
        sendNewCourierNoFirstname();
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

    @Step("Создание курьера с уже использованным login")
    public void sendNewDoubleCourier() {
        String json = "{\"login\": \"" + randomLogin + "\" , \"password\": \"123\", \"firstName\": \"max1\"}";
        Response responseOne =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        responseOne.then().log().all().statusCode(201)
                        .and().body("ok", equalTo(true));
        Response responseTwo =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        responseTwo.then().log().all().statusCode(409)
                        .and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Step("Создание курьера - пустое поле login")
    public void sendNewCourierNullLogin() {
        String json = "{\"login\": \"\", \"password\": \"123\", \"firstName\": \"max1\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Step("Создание курьера - не передаётся поле login")
    public void sendNewCourierNoLogin() {
        String json = "{\"password\": \"123\", \"firstName\": \"max1\"}";
        Response responseTwo =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        responseTwo.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создание курьера - пустое поле password")
    public void sendNewCourierNullPassword() {
        String json = "{\"login\": \"t+123\", \"password\": \"\", \"firstName\": \"max1\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Step("Создание курьера - не передаётся поле password")
    public void sendNewCourierNoPassword() {
        String json = "{\"login\": \"t+123\", \"firstName\": \"max1\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
                        response.then().log().all().statusCode(400)
                        .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Step("Создание курьера - пустое поле password")
    public void sendNewCourierNullFirstname() {
        String json = "{\"login\": \"" + randomLogin + "\", \"password\": \"123\", \"firstName\": \"\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
    @Step("Создание курьера - не передаётся поле password")
    public void sendNewCourierNoFirstname() {
        String json = "{\"login\": \"" + randomLogin + "\", \"password\": \"123\"}";
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(json)
                        .when()
                        .post("/api/v1/courier");
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }


    @Step("Очистка базы")
    public void deleteData() {
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

        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/api/v1/courier/" + idCourier);
    }

}
