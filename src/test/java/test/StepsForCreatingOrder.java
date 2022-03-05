package test;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class StepsForCreatingOrder {
    @Step("Отменяем заказ")
    public void cancelOrder() {
        given().log().all()
                .header("Content-type", "application/json")
                .and()
                .when()
                .param("track", DataHelper.track)
                .put("/api/v1/orders/cancel");
    }
}
