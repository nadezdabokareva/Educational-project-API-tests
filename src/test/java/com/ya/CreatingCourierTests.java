package com.ya;

import com.ya.RestClients.CourierRestClient;
import com.ya.entity.Courier;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@Story("Тесты на создание курьера")
public class CreatingCourierTests {

    private Courier courier;
    private String safePassword;
    private String safeLogin;

    @Step("Генерация рандомных логина, пароля и имени для курьера")
    protected static Courier createRandomCourier() {
        String courierLogin = RandomStringUtils.randomAlphabetic(10);
        String courierPassword = RandomStringUtils.randomAlphabetic(10);
        String courierFirstName = RandomStringUtils.randomAlphabetic(10);
        Allure.addAttachment("Логин", courierLogin);
        Allure.addAttachment("Пароль", courierPassword);
        Allure.addAttachment("Имя", courierFirstName);
        return new Courier(courierLogin, courierPassword, courierFirstName);
    }

    @Before
    public void setUp() {
        courier = createRandomCourier();
        CourierRestClient.login(courier);
        safePassword = courier.getPassword();
        safeLogin = courier.getLogin();
    }

    @After
    public void tearDown() {
        courier.setLogin(safeLogin);
        courier.setPassword(safePassword);
        CourierRestClient.delete(courier);
    }

    @Test
    @DisplayName("Курьера можно создать со всеми необходимыми данными")
    public void testCreatingOneNewCourierWithValidCredentials() {
        ValidatableResponse validatableResponse = CourierRestClient.createNewRestCourier(courier);
        assertThat(validatableResponse.extract().statusCode(), equalTo(201));
        assertThat(validatableResponse.extract().path("ok"), equalTo(true));
    }

    @Test
    @DisplayName("Нельзя создать курьера с одинаковыми логином, паролем и фамилией")
    public void testCreatingCourierClone() {
        CourierRestClient.createNewRestCourier(courier);
        ValidatableResponse createResponse = CourierRestClient.createNewRestCourier(courier);
        assertThat(createResponse.extract().statusCode(), equalTo(409));
        assertThat(createResponse.extract().path("message"), equalTo("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void testCreatingCourierWithoutLoginCredential() {
        Courier courier = createRandomCourier();
        courier.setLogin(null);
        ValidatableResponse createResponse = CourierRestClient.createNewRestCourier(courier);
        assertThat(createResponse.extract().statusCode(), equalTo(400));
        assertThat(createResponse.extract().path("message"), equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void testCreatingCourierWithoutPasswordCredential() {
        courier.setPassword(null);
        ValidatableResponse createResponse = CourierRestClient.createNewRestCourier(courier);
        assertThat(createResponse.extract().statusCode(), equalTo(400));
        assertThat(createResponse.extract().path("message"), equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать двух разных курьеров с однинаковым логином")
    public void testCreatingCourierSameLogin() {
        Courier secondCourier = (Courier) courier.clone();
        secondCourier.setFirstName(RandomStringUtils.randomAlphabetic(10));
        CourierRestClient.createNewRestCourier(secondCourier);
        ValidatableResponse createSecondResponse = CourierRestClient.createNewRestCourier(secondCourier);
        assertThat(createSecondResponse.extract().statusCode(), equalTo(409));
        assertThat(createSecondResponse.extract().path("message"), equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
