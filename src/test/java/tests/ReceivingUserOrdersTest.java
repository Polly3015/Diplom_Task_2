package tests;

import objects.ObjectsIngredients;
import objects.ObjectsUser;
import requests.Order;
import requests.ReceiveIngredient;
import requests.RegisterAndDelete;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ReceivingUserOrdersTest {

    ObjectsUser user = new ObjectsUser("test130@test.ru", "123456", "Polly");

    RegisterAndDelete register = new RegisterAndDelete();

    ReceiveIngredient receiveIngredient = new ReceiveIngredient();
    ObjectsIngredients correctIngredients = new ObjectsIngredients(
            List.of(receiveIngredient.searchIngredient()));

    Order order = new Order();

    private String userToken = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru/";

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
