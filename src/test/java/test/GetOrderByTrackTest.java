package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;

public class GetOrderByTrackTest extends StepsForTests {

    @Before
    public void setUp() {
        baseUrl();
        //создаём заказ
        sendPostOrder();
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (успех)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByTrackTest() {
        //получаем заказ по трек-номеру
        Response response = getOrderByTrack();
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
        //получаем заказ по трек-номеру - запрос без номера
        Response response = getOrderByNullTrack();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Проверка ручки /api/v1/orders/track - получить заказ по его номеру (404 - несуществующий номер)")
    @Description("Проверяем верный ли код ответа и содержимое ответа")
    public void getOrderByFalseTrackTest() {
        //получаем заказ по трек-номеру - несуществующий номер
        Response response = getOrderByFalseTrack();
        //проверка тела ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказ не найден"));
    }

    //отмена заказа
    @After
    public void deleteData() {
        cancelOrder();
    }
}
