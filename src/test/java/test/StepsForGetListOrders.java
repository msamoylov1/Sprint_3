package test;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class StepsForGetListOrders {
    @Step("Получаем список заказов")
    public Response getListOrders() {
        Response response = given().log().all()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders");
        return response;
    }
}
