package test;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StepsForLoginCourier {

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
}
