package com.ya.RestClients;

import com.ya.entity.Courier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


public class CourierRestClient extends BasicRestClient {

    public static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Авторизация курьера")
    public static int login(Courier courier) {
        courier.setId(given()
                .spec(getBaseSpec())
                .body(courier.getCourierCredentials())
                .when()
                .post(COURIER_PATH + "login")
                .then().log().all()
                .extract()
                .path("id"));
        return courier.getId();
    }

    @Step("Удаление курьера")
    public static boolean delete(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .body(courier.getCourierCredentials())
                .when()
                .delete(COURIER_PATH + courier.getId())
                .then().log().all()
                .extract()
                .path("ok");
    }

    @Step("Создание курьера")
    public static ValidatableResponse createNewRestCourier(Courier courier) {
        return given()
                .spec(getBaseSpec())
                .and()
                .body(courier)
                .when()
                .post(COURIER_PATH)
                .then();
    }
}
