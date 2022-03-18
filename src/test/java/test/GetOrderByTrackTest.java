package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderByTrackTest extends BaseTest {

    @Before
    public void setUp() {
        StepsForGetOrderByTrack stepsForGetOrderByTrack = new StepsForGetOrderByTrack();
        //создаём заказ
        stepsForGetOrderByTrack.sendPostOrder();
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (успех)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByTrackTest() {
        StepsForGetOrderByTrack stepsForGetOrderByTrack = new StepsForGetOrderByTrack();
        //получаем заказ по трек-номеру
        Response response = stepsForGetOrderByTrack.getOrderByTrack();
        //проверка
        response.then().log().all().statusCode(200)
                .and().body("order", notNullValue())
                .and().body("order.id", notNullValue())
                .and().body("order.firstName", notNullValue())
                .and().body("order.lastName", notNullValue())
                .and().body("order.address", notNullValue())
                .and().body("order.metroStation", notNullValue())
                .and().body("order.phone", notNullValue())
                .and().body("order.rentTime", notNullValue())
                .and().body("order.deliveryDate", notNullValue())
                .and().body("order.track", equalTo(DataHelper.track));
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (400 - запрос без номера)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByNullTrackTest() {
        StepsForGetOrderByTrack stepsForGetOrderByTrack = new StepsForGetOrderByTrack();
        //получаем заказ по трек-номеру - запрос без номера
        Response response = stepsForGetOrderByTrack.getOrderByNullTrack();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (404 - несуществующий номер)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByFalseTrackTest() {
        StepsForGetOrderByTrack stepsForGetOrderByTrack = new StepsForGetOrderByTrack();
        //получаем заказ по трек-номеру - несуществующий номер
        Response response = stepsForGetOrderByTrack.getOrderByFalseTrack();
        //проверка тела ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказ не найден"));
    }

    //отмена заказа
    @After
    public void deleteData() {
        StepsForGetOrderByTrack stepsForGetOrderByTrack = new StepsForGetOrderByTrack();
        stepsForGetOrderByTrack.cancelOrder();
    }
}
