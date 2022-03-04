package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static test.DataHelper.getRandomString;

public class AcceptOrderTest extends StepsForTests {

    @Before
    public void setUp() {
        baseUrl();
        DataHelper.randomLogin = getRandomString(5);
        //создаём заказ и получаем трек-номер заказа
        sendPostOrder();
        //получаем заказ по номеру и записываем его id
        getOrderByTrack();
        //создаём курьера
        sendNewCourier();
        //логинимся курьером и получаем id
        loginCourierAndGetId();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (успех)")
    @Description("Все передаваемые данные (id курьера/заказа) - валидные")
    public void acceptOrderFullFieldTest() {
        //принимаем заказ
        Response response = acceptNewOrder();
        //проверка тела ответа
        response.then().log().all().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id курьера)")
    @Description("id курьера не передаётся в запросе")
    public void acceptOrderNoIdCourierTest() {
        //принимаем заказ
        Response response = acceptNoIdCourierNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (400 - нет id заказа)")
    @Description("id заказа не передаётся в запросе")
    public void acceptOrderNoIdCOrderTest() {
        //принимаем заказ
        Response response = acceptNoIdOrderNewOrder();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id заказа)")
    @Description("Передаётся несуществующий id заказа в запросе")
    public void acceptOrderFalseIdOrderTest() {
        //принимаем заказ
        Response response = acceptFalseIdOrderNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Заказа с таким id не существует"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders/accept/:id - принять заказ (404 - несуществующий id курьера)")
    @Description("Передаётся несуществующий id курьера в запросе")
    public void acceptOrderFalseIdCourierTest() {
        //принимаем заказ
        Response response = acceptFalseIdCourierNewOrder();
        //проверяем тело ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id не существует"));
    }


    @After
    public void deleteData() {
        //отменим заказ
        cancelOrder();
        //завершаем заказ
        finishOrder();
        //удаление курьера
        deleteNewCourier();
    }
}
