package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static test.DataHelper.getRandomString;

public class LoginCourierTest extends StepsForTests {

    @Before
    public void setUp() {
        baseUrl();
        DataHelper.randomLogin = getRandomString(5);
        //создаём курьера
        sendNewCourier();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (успех)")
    @Description("Отправляются все 2 поля - корректно заполненные")
    public void trueLoginCourierTest() {
        //логин за ранее созданного курьера
        Response response = loginCourier();
        //проверка тела ответа
        response.then().log().all().statusCode(200)
                .and().body("id", notNullValue());
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (404 - несуществующий логин)")
    @Description("Отправляются все 2 поля - несуществующий логин, ожидаем ошибку 404")
    public void loginFalseLoginCourierTest() {
        Response response = getFalseLoginCourier();
        //проверка тела ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (404 - несуществующий пароль)")
    @Description("Отправляются все 2 поля - несуществующий пароль, ожидаем ошибку 404")
    public void loginFalsePassCourierTest() {
        Response response = getFalsePassCourier();
        //проверка тела ответа
        response.then().log().all().statusCode(404)
                .and().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (400 - пустое/пробел в поле логин)")
    @Description("Отправляются 1 поле(password)/2 поля, но в login пусто - ожидаем ошибку 400")
    public void loginNoLoginCourierTest() {
        //кейс 1 - пустое поле логин
        Response response = getNullLoginCourier();
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));

        //кейс 2 - пробел в поле логин
        getNoLoginCourier();
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier/login - логин курьера в системе (400 - пустое/пробел в поле пароль)")
    @Description("Отправляются 1 поле(login)/2 поля, но в password пусто - ожидаем ошибку 400")
    public void loginNoPassCourierTest() {
        //пробел в поле пароль
        Response response = getNoPassCourier();
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для входа"));
    }

    @After
    public void deleteCourier() {
        //получаем id курьера
        loginCourierAndGetId();

        //удаление курьера
        deleteNewCourier();

    }
}
