package com.ya;

import com.ya.RestClients.OrderRestClient;
import com.ya.entity.Order;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(Parameterized.class)
@Story("Тесты на создание заказа")
public class CreatingOrderTests {

    private static Order order;

    public CreatingOrderTests(String color) {
        order = generateRandomOrder();
        order.setColor(new String[]{color});
    }

    @Parameterized.Parameters
    public static Object[][] getDifferentColor() {
        return new Object[][]{{"BLACK"}, {"GRAY"}, {" "}, {"[BLACK, GRAY]"}};
    }

    @Step("Генерация рандомных данных для создания заказа")
    private static Order generateRandomOrder() {
        return new Order(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10), RandomUtils.nextInt(0, 100), LocalDate.now().toString(), RandomStringUtils.randomAlphabetic(10), new String[]{RandomStringUtils.randomAlphabetic(10)});
    }

    @AfterClass
    public static void afterClass() {
        OrderRestClient.cancelOrder(order);
    }

    @Test
    @DisplayName("Создание заказа с разными цветами")
    public void testCreatingOrderWithThwDifferentColors() {
        ValidatableResponse validatableResponse = OrderRestClient.createOrder(order);
        assertThat(validatableResponse.extract().statusCode(), equalTo(201));
        assertThat(validatableResponse.extract().response(), is(not(equalTo(0))));
        assertThat(validatableResponse.extract().body().path("track"), is(not(equalTo(null))));
    }
}
