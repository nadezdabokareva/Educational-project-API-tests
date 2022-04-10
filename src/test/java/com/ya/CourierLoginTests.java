package com.ya;

import com.ya.RestClients.CourierRestClient;
import com.ya.entity.Courier;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.ya.CreatingCourierTests.createRandomCourier;
import static org.junit.Assert.*;

@Story("Тесты на создание курьера")
public class CourierLoginTests {

    private Courier courier;
    private String safePassword;
    private String safeLogin;

    @Before
    @DisplayName("Курьер может авторизоваться")
    public void setUp() {
        courier = createRandomCourier();
        CourierRestClient.createNewRestCourier(courier);
        ValidatableResponse validatableResponse = CourierRestClient.login(courier);
        assertNotNull(validatableResponse.extract().body().path("id"));
        assertEquals(200, validatableResponse.extract().statusCode());
        safePassword = courier.getPassword();
        safeLogin = courier.getLogin();
    }

    @After
    @DisplayName("Курьера можно удалить")
    public void tearDown() {
        courier.setLogin(safeLogin);
        courier.setPassword(safePassword);
        ValidatableResponse validatableResponse = CourierRestClient.delete(courier);
        assertEquals(200, validatableResponse.extract().statusCode());
        assertEquals(true, validatableResponse.extract().body().path("ok"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {
        courier.setLogin(null);
        ValidatableResponse validatableResponse = CourierRestClient.login(courier);
        assertEquals(400, validatableResponse.extract().statusCode());
        assertEquals("Недостаточно данных для входа", validatableResponse.extract().body().path("message"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с некорректным логином")
    public void testLoginCourierIncorrectLogin() {
        courier.setLogin("null");
        ValidatableResponse validatableResponse = CourierRestClient.login(courier);
        assertEquals(404, validatableResponse.extract().statusCode());
        assertEquals("Учетная запись не найдена", validatableResponse.extract().body().path("message"));
    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("Курьер не может авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {
        courier.setPassword(null);
        ValidatableResponse validatableResponse = CourierRestClient.login(courier);
        assertEquals(400, validatableResponse.extract().statusCode());
        assertEquals("Недостаточно данных для входа", validatableResponse.extract().body().path("message"));
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с некорректным паролем")
    public void testLoginCourierIncorrectPassword() {
        courier.setPassword("null");
        ValidatableResponse validatableResponse = CourierRestClient.login(courier);
        assertEquals(404, validatableResponse.extract().statusCode());
        assertEquals("Учетная запись не найдена", validatableResponse.extract().body().path("message"));
    }

    @Test
    @DisplayName("Курьер может авторизоваться и после логина возвращается id курьера")
    public void testCourierCanLoginAndHasId() {
        assertNotEquals(courier.getId(), 0);
    }
}
