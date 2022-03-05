package test;

import io.restassured.RestAssured;
import org.junit.Before;

import static test.DataHelper.getRandomString;

public class BaseTest {

    @Before
    public void baseUrl() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
        DataHelper.randomLogin = getRandomString(5);
    }
}
