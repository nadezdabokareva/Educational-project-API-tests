package com.ya;

import com.ya.RestClients.CourierRestClient;
import com.ya.entity.Courier;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static com.ya.CreatingCourierTests.createRandomCourier;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.*;

@Story("Тесты на создание курьера")
public class CourierLoginTests {

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void testCourierCanLogin() {
        Courier courier = createRandomCourier();

        safetyCreateRestCourier(courier);

        assertNotNull(CourierRestClient.login(courier));

        safetyDeleteCourier(courier);

    }

    @Test
    @DisplayName("Курьер не может авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {
        Courier courier = createRandomCourier();
        String courierLogin = courier.getLogin();

        safetyCreateRestCourier(courier);

        courier.setLogin(null);

        invalidLogin("Выполнен вход без логина", NullPointerException.class, courier);

        courier.setLogin(courierLogin);

        safetyDeleteCourier(courier);

    }


    @Test
    @DisplayName("Курьер не может авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {
        Courier courier = createRandomCourier();
        String courierPassword = courier.getPassword();

        safetyCreateRestCourier(courier);

        courier.setPassword(null);

        invalidLogin("Выполнен вход без пароля", IllegalStateException.class, courier);

        courier.setPassword(courierPassword);

        safetyDeleteCourier(courier);

    }

    @Test
    @DisplayName("Курьер не может авторизоваться с некорректным паролем")
    public void testLoginCourierIncorrectPassword() {
        Courier courier = createRandomCourier();
        String courierPassword = courier.getPassword();

        safetyCreateRestCourier(courier);

        courier.setPassword("null");

        invalidLogin("Выполнен вход без пароля", NullPointerException.class, courier);

        courier.setPassword(courierPassword);

        safetyDeleteCourier(courier);
    }

    @Test
    @DisplayName("Курьер не может авторизоваться с некорректным логином")
    public void testLoginCourierIncorrectLogin() {
        Courier courier = createRandomCourier();
        String courierLogin = courier.getLogin();

        safetyCreateRestCourier(courier);

        courier.setLogin("null");

        invalidLogin("Выполнен вход без логина", NullPointerException.class, courier);

        courier.setLogin(courierLogin);

        safetyDeleteCourier(courier);
    }
    @Test
    @DisplayName("Курьер не может авторизоваться с пустым логином")
    public void testLoginNotRestCourier() {
        Courier courier = createRandomCourier();
        String courierLogin = courier.getLogin();

        courier.setLogin("null");

        invalidLogin("Выполнен вход без логина", NullPointerException.class, courier);

        courier.setLogin(courierLogin);

        safetyDeleteCourier(courier);
    }

    @Test
    @DisplayName("Курьер может авторизоваться и после логина возвращается id курьера")
    public void testCourierCanLoginAndHasId() {
        Courier courier = createRandomCourier();

        safetyCreateRestCourier(courier);

        CourierRestClient.login(courier);

        safetyDeleteCourier(courier);

        assertNotEquals(courier.getId(), 0);
    }


    @Step("Создание курьера без наполнения данными")
    private static void safetyCreateRestCourier(Courier courier) {
        try {
            ValidatableResponse createResponse = CourierRestClient.createNewRestCourier(courier);
            int statusCode = createResponse.extract().statusCode();
            boolean creatingMessage = createResponse.extract().path("ok");
            assertThat(statusCode, equalTo(201));
            assertThat(creatingMessage, equalTo(true));
        } catch (AssertionError assertionError) {
            throw new AssertionError("Некорректно произошло создание курьера");
        }
    }

    @Step("Удаление курьера с проверкой на ошибки")
    protected static void safetyDeleteCourier(Courier courier){
        try {
            CourierRestClient.login(courier);
            assertThat(CourierRestClient.delete(courier), equalTo(true));
        } catch (AssertionError assertionError) {
            throw new AssertionError("После теста курьер не был удален");
        } catch (NullPointerException nullPointerException){

        }
    }

    @Step("Авторизация курьера с проверкой на ошибки")
    private static <T extends Exception> void invalidLogin(String message,
                                                           Class<T> exceptionType, Courier courier) {
        try {
            CourierRestClient.login(courier);
        } catch (Exception ex){
            if (courier.getId() != 0 || !exceptionType.isInstance(ex)) {
                throw new AssertionError(message);
            }
        }
    }
}
