package com.ya.RestClients;

import com.google.gson.JsonObject;
import com.ya.entity.Order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderRestClient extends BasicRestClient {
    public static final String ORDER_PATH = "/api/v1/orders/";

    @Step("Закрытие заказа")
    public static ValidatableResponse closeOrder(Order order) {
        return given().spec(getBaseSpec()).body(order).params(new HashMap<>() {{
            put("id", order.getId());
        }}).when().put(ORDER_PATH + "finish/" + order.getId()).then().log().all();
    }

    @Step("Отмена заказа")
    public static ValidatableResponse cancelOrder(Order order) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("track", order.getTrack());
        return given().spec(getBaseSpec()).body(jsonObject).when().put(ORDER_PATH + "cancel").then().log().all();
    }

    @Step("Получить список заказов")
    public static ValidatableResponse getListOrders(Integer courierId, String nearestStation, Integer limit, Integer page) {
        Map mapParams = new HashMap<>();
        if (!(courierId == null)) mapParams.put("courierId", courierId);
        if (!(nearestStation == null)) mapParams.put("nearestStation", nearestStation);
        if (!(limit == null)) mapParams.put("limit", limit);
        if (!(page == null)) mapParams.put("courierId", page);

        return given().spec(getBaseSpec()).params(mapParams).when().get(ORDER_PATH).then().log().all();
    }

    @Step("Получить заказ по номеру")
    public static ValidatableResponse getOrderByNumber(Integer t) {
        Map mapParams = new HashMap<>();
        if (!t.equals(null)) mapParams.put("t", t);

        return given().spec(getBaseSpec()).params(mapParams).when().get(ORDER_PATH + "track").then().log().all();
    }

    @Step("Принять заказ")
    public static ValidatableResponse acceptOrderByNumberAndCourier(Integer id, Integer courierId) {
        Map mapParams = new HashMap<>();
        if (!id.equals(null)) mapParams.put("id", id);
        if (!courierId.equals(null)) mapParams.put("courierId", courierId);

        return given().spec(getBaseSpec()).params(mapParams).when().put(ORDER_PATH + "accept").then().log().all();
    }

    @Step("Создание заказа")
    public static ValidatableResponse createOrder(Order order) {
        ValidatableResponse validatableResponse = given().spec(getBaseSpec()).body(order).when().post(ORDER_PATH).then().log().all();
        order.setTrack(validatableResponse.extract().path("track"));
        return validatableResponse;
    }
}
