package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class AcceptOrderTest extends BaseTest {

    @Before
    public void setUp() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //создаём заказ и получаем трек-номер заказа
        stepsForAcceptOrder.sendPostOrder();
        //получаем заказ по номеру и записываем его id
        stepsForAcceptOrder.getOrderByTrack();
        //создаём курьера
        stepsForAcceptOrder.sendNewCourier();
        //логинимся курьером и получаем id
        stepsForAcceptOrder.loginCourierAndGetId();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (успех)")
    @Description("Все передаваемые данные (id курьера/заказа) - валидные")
    public void acceptOrderFullFieldTest() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //принимаем заказ
        Response response = stepsForAcceptOrder.acceptNewOrder();
        //проверка тела ответа
        response.then().log().all().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id курьера)")
    @Description("id курьера не передаётся в запросе")
    public void acceptOrderNoIdCourierTest() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //принимаем заказ
        Response response = stepsForAcceptOrder.acceptNoIdCourierNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id заказа)")
    @Description("id заказа не передаётся в запросе")
    public void acceptOrderNoIdCOrderTest() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //принимаем заказ
        Response response = stepsForAcceptOrder.acceptNoIdOrderNewOrder();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id заказа)")
    @Description("Передаётся несуществующий id заказа в запросе")
    public void acceptOrderFalseIdOrderTest() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //принимаем заказ
        Response response = stepsForAcceptOrder.acceptFalseIdOrderNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id курьера)")
    @Description("Передаётся несуществующий id курьера в запросе")
    public void acceptOrderFalseIdCourierTest() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //принимаем заказ
        Response response = stepsForAcceptOrder.acceptFalseIdCourierNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id не существует"));
    }


    @After
    public void deleteData() {
        StepsForAcceptOrder stepsForAcceptOrder = new StepsForAcceptOrder();
        //отменим заказ
        stepsForAcceptOrder.cancelOrder();
        //завершаем заказ
        stepsForAcceptOrder.finishOrder();
        //удаление курьера
        stepsForAcceptOrder.deleteNewCourier();
    }
}
