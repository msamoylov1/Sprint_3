package test;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

@RunWith(Parameterized.class)
public class CreatingOrdersTest extends StepsForTests {

    private final List<String> color;

    public CreatingOrdersTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] colorData() {
        return new Object[][] {
                {List.of("BLACK")}, //выбран 1 цвет - BLACK
                {List.of("Самка", "GREY")}, //выбраны 2 цвета - BLACK и GREY
                {List.of("GREY")}, //выбран 1 цвет - GREY
                {List.of()} //не выбран цвет
        };
    }

    @Before
    public void setUp() {
        baseUrl();
    }

    @Test
    @DisplayName("Тест ручки /api/v1/orders - создания заказа")
    @Description("Параметризованный тест - с выбранным цветом(1/2 цвета)/цвет не выбран")
    public void creatingOrderFullFieldTest() {
        CreateOrder createOrder = new CreateOrder(
                "Naruto",
                "Uchiha",
                "Konoha, 142 apt.",
                4,
                "+7 800 355 35 35",
                5,
                "2020-06-06",
                "Saske, come back to Konoha",
                color);
        Response response =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createOrder)
                        .when()
                        .post("/api/v1/orders");
                        response.then().log().all().statusCode(201)
                        .and().body("track", notNullValue());
        JsonPath jsonPathOrder = response.jsonPath();
        DataHelper.track = jsonPathOrder.get("track");
    }

    //отмена заказа
    @After
    public void deleteData() {
        cancelOrder();
    }

}
