package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class DeleteCourierTest extends BaseTest {

    @Before
    public void setUp() {
        StepsForDeleteCourier stepsForDeleteCourier = new StepsForDeleteCourier();
        //создаём курьера
        stepsForDeleteCourier.sendNewCourier();
        //логинимся курьером и получаем id
        stepsForDeleteCourier.loginCourierAndGetId();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (успех)")
    @Description("Отправляются существующий id курьера")
    public void deleteCourierTest() {
        StepsForDeleteCourier stepsForDeleteCourier = new StepsForDeleteCourier();
        //удаляем ранее созданного курьера
        Response response = stepsForDeleteCourier.deleteNewCourier();
        //проверка ответа
        response.then().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    //баг - в тексте сообщения об ошибке "Курьера с таким id нет." - лишняя точка
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 404)")
    @Description("Отправляются несуществующий id курьера")
    public void deleteCourierFalseIdTest() {
        StepsForDeleteCourier stepsForDeleteCourier = new StepsForDeleteCourier();
        //удаление курьера
        Response response = stepsForDeleteCourier.deleteFalseIdNewCourier();
        //проверка ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id нет"));
    }

    //баг - в тексте сообщения об ошибке "Not Found.", ошибка 404 (должна быть 400)
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 400)")
    @Description("Не отправляются id курьера")
    public void deleteCourierNoIdTest() {
        StepsForDeleteCourier stepsForDeleteCourier = new StepsForDeleteCourier();
        //удаление курьера
        Response response = stepsForDeleteCourier.deleteNoIdNewCourier();
        //проверка ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @After
    public void deleteData() {
        StepsForDeleteCourier stepsForDeleteCourier = new StepsForDeleteCourier();
        //удаление курьера
        stepsForDeleteCourier.deleteNewCourier();
    }
}
