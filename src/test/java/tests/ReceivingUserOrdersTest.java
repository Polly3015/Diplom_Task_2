package tests;

import objects.Ingredients;
import objects.User;
import requests.Order;
import requests.ReceiveIngredient;
import requests.RegisterAndDelete;
import io.qameta.allure.Step;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReceivingUserOrdersTest extends BaseApiTest{

    User user = new User("test130@test.ru", "123456", "Polly");

    RegisterAndDelete register = new RegisterAndDelete();

    ReceiveIngredient receiveIngredient = new ReceiveIngredient();
    Ingredients correctIngredients = new Ingredients(
            List.of(receiveIngredient.searchIngredient()));

    Order order = new Order();

    private String userToken = null;

    @BeforeEach
    public void setUp() {
        userToken = register.successRegisterUser(user);
        order.successCreateOrderWithAuthorization(userToken, correctIngredients);
    }

    @Test
    @DisplayName("Запрос списка заказов")
    @Description("Запрос списка заказов с авторизацией и без")
    public void receiveOrders() {
        withoutAuthorization();
        withAuthorization();
    }

    @Step("Запрос без авторизации - ошибка")
    private void withoutAuthorization() {
        order.errorGetOrderWithoutAuthorization();
    }

    @Step("Запрос c авторизацией - успех")
    private void withAuthorization() {
        order.successGetOrderWithAuthorization(userToken);
    }

    @AfterEach
    public void deleteUser(){
        register.deleteUser(userToken);
    }

}
