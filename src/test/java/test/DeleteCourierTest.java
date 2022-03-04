package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static test.DataHelper.getRandomString;

public class DeleteCourierTest extends StepsForTests {

    @Before
    public void setUp() {
        baseUrl();
        DataHelper.randomLogin = getRandomString(5);
        //создаём курьера
        sendNewCourier();
        //логинимся курьером и получаем id
        loginCourierAndGetId();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (успех)")
    @Description("Отправляются существующий id курьера")
    public void deleteCourierTest() {
        //удаляем ранее созданного курьера
        Response response = deleteNewCourier();
        //проверка ответа
        response.then().statusCode(200)
                .and().body("ok", equalTo(true));
    }

    //баг - в тексте сообщения об ошибке "Курьера с таким id нет." - лишняя точка
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 404)")
    @Description("Отправляются несуществующий id курьера")
    public void deleteCourierFalseIdTest() {
        //удаление курьера
        Response response = deleteFalseIdNewCourier();
        //проверка ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Курьера с таким id нет"));
    }

    //баг - в тексте сообщения об ошибке "Not Found.", ошибка 404 (должна быть 400)
    @Test
    @DisplayName("Тест ручки /api/v1/courier/:id - удаление курьера (ошибка 400)")
    @Description("Не отправляются id курьера")
    public void deleteCourierNoIdTest() {
        //удаление курьера
        Response response = deleteNoIdNewCourier();
        //проверка ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для удаления курьера"));
    }

    @After
    public void deleteData() {
        //удаление курьера
        deleteNewCourier();
    }
}
