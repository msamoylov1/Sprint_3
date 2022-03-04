package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static test.DataHelper.getRandomString;

public class CreatingCourierTest extends StepsForTests {

    @Before
    public void setUp() {
        baseUrl();
        DataHelper.randomLogin = getRandomString(5);
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (успех)")
    @Description("Отправляются все 3 поля - корректно заполненные")
    public void createCourierWellCaseTest() {
        //успешно создаём курьера
        Response response = sendNewCourier();
        //проверяем тело ответа
        response.then().log().all().statusCode(201)
                .and().body("ok", equalTo(true));
    }

    //негативные кейсы
    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера дублирование login")
    @Description("Попытка создать курьера с уже использующимся логином - ошибка 409")
    public void createTwoCourierOneLoginTest() {
        //пытаемся создать нового курьера с таким же логином
        Response response = sendNewDoubleCourier();
        //проверяем тело ответа
        response.then().log().all().statusCode(409)
                .and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера")
    @Description("Отправляются 2 поля(password/firstName)/3 поля, но в login пусто - ошибка 400")
    public void createCourierNoLoginTest() {
        //кейс 1 - пустое поле login
        Response response = sendNewCourierNullLogin();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - пробел в поле login
        sendNewCourierNoLogin();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (ошибка 400 - не все данные)")
    @Description("Отправляются 2 поля(login/firstName)/3 поля, но в password пусто - ошибка 400")
    public void createCourierNoPasswordTest() {
        //кейс 1 - пустое поле пароль
        Response response = sendNewCourierNullPassword();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - нет поля пароль
        sendNewCourierNoPassword();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (ошибка 400 - не все данные)")
    @Description("Отправляются 2 поля(login/password)/3 поля, но в firstName пусто - ошибка 400")
    //в этом тесте есть баг - firstName необязательное при создании курьера
    public void createCourierNoFirstNameTest() {
        //кейс 1 - пустое поле фамилия
        Response response = sendNewCourierNullFirstname();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - нет поля фамилия
        sendNewCourierNoFirstname();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteData() {
        //получаем id курьера
        loginCourierAndGetId();
        //удаление курьера
        deleteNewCourier();
    }
}
