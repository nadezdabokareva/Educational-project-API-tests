package com.ya.RestClients;

import com.google.gson.JsonObject;
import com.ya.entity.Courier;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierRestClient extends BasicRestClient {

    public static final String COURIER_PATH = "/api/v1/courier/";

    @Step("Авторизация курьера")
    public static ValidatableResponse login(Courier courier) {
        ValidatableResponse validatableResponse = given().spec(getBaseSpec()).body(courier.getCourierCredentials()).when().post(COURIER_PATH + "login").then().log().all();
        try {
            courier.setId(validatableResponse.extract().path("id"));
        } catch (NullPointerException nullPointerException) {
        }
        return validatableResponse;
    }

    @Step("Удаление курьера")
    public static ValidatableResponse delete(Courier courier) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", courier.getId());
        return given().spec(getBaseSpec()).body(jsonObject).when().delete(COURIER_PATH + courier.getId()).then().log().all();
    }

    @Step("Создание курьера")
    public static ValidatableResponse createNewRestCourier(Courier courier) {
        return given().spec(getBaseSpec()).and().body(courier).when().post(COURIER_PATH).then();
    }
}
