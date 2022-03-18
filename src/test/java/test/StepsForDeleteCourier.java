package test;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StepsForDeleteCourier {

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
}
