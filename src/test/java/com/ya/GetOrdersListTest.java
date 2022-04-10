package com.ya;

import com.ya.RestClients.OrderRestClient;
import io.qameta.allure.Story;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

@Story("Тесты на получение списка заказов")
public class GetOrdersListTest {

    @Test
    @DisplayName("Тест на получение списка заказов")
    public void testGetBodyHasOrdersList() {
        ValidatableResponse validatableResponse = OrderRestClient.getListOrders(null, null, null, null);
        assertThat(validatableResponse.extract().body(), is(not(equalTo(null))));
        assertThat(validatableResponse.extract().statusCode(), equalTo(200));
    }
}
