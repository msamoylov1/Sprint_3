package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
public class CreatingCourierTest extends BaseTest {

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (успех)")
    @Description("Отправляются все 3 поля - корректно заполненные")
    public void createCourierWellCaseTest() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //успешно создаём курьера
        Response response = stepsForCreatingCourier.sendNewCourier();
        //проверяем тело ответа
        response.then().log().all().statusCode(201)
                .and().body("ok", equalTo(true));
    }

    //негативные кейсы
    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера дублирование login")
    @Description("Попытка создать курьера с уже использующимся логином - ошибка 409")
    public void createTwoCourierOneLoginTest() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //пытаемся создать нового курьера с таким же логином
        Response response = stepsForCreatingCourier.sendNewDoubleCourier();
        //проверяем тело ответа
        response.then().log().all().statusCode(409)
                .and().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера")
    @Description("Отправляются 2 поля(password/firstName)/3 поля, но в login пусто - ошибка 400")
    public void createCourierNoLoginTest() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //кейс 1 - пустое поле login
        Response response = stepsForCreatingCourier.sendNewCourierNullLogin();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - пробел в поле login
        stepsForCreatingCourier.sendNewCourierNoLogin();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (ошибка 400 - не все данные)")
    @Description("Отправляются 2 поля(login/firstName)/3 поля, но в password пусто - ошибка 400")
    public void createCourierNoPasswordTest() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //кейс 1 - пустое поле пароль
        Response response = stepsForCreatingCourier.sendNewCourierNullPassword();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - нет поля пароль
        stepsForCreatingCourier.sendNewCourierNoPassword();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Тест ручки /api/v1/courier - создания курьера (ошибка 400 - не все данные)")
    @Description("Отправляются 2 поля(login/password)/3 поля, но в firstName пусто - ошибка 400")
    //в этом тесте есть баг - firstName необязательное при создании курьера
    public void createCourierNoFirstNameTest() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //кейс 1 - пустое поле фамилия
        Response response = stepsForCreatingCourier.sendNewCourierNullFirstname();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
        //кейс 2 - нет поля фамилия
        stepsForCreatingCourier.sendNewCourierNoFirstname();
        //проверка тела ответа
        response.then().log().all().statusCode(400)
                .and().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void deleteData() {
        StepsForCreatingCourier stepsForCreatingCourier = new StepsForCreatingCourier();
        //получаем id курьера
        stepsForCreatingCourier.loginCourierAndGetId();
        //удаление курьера
        stepsForCreatingCourier.deleteNewCourier();
    }
}
