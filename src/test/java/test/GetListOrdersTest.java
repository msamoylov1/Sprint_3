package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOrdersTest extends BaseTest {

    @Test
    @DisplayName("Проверка ручки /api/v1/orders - получить список заказов")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getListOrdersTest() {
        StepsForGetListOrders stepsForGetListOrders = new StepsForGetListOrders();
        //Получаем список заказов
        Response response = stepsForGetListOrders.getListOrders();
        //проверяем тело ответа
        response.then().body("orders", notNullValue())
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
