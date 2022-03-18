package test;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StepsForCreatingCourier {

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
