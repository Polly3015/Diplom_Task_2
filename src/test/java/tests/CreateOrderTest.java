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

public class CreateOrderTest {

    ObjectsUser user = new ObjectsUser("test130@test.ru", "123456", "Polly");

    RegisterAndDelete register = new RegisterAndDelete();

    ReceiveIngredient receiveIngredient = new ReceiveIngredient();
    ObjectsIngredients correctIngredients = new ObjectsIngredients(
             List.of(receiveIngredient.searchIngredient()));
    ObjectsIngredients incorrectIngredients;

    Order order = new Order();

    private String userToken = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru/";

        userToken = register.successRegisterUser(user);
    }


    // Создание заказов с авторизациией
    @Test
    @DisplayName("Созданиие заказов с авторизациией")
    @Description("Успешное создание заказа при корретных значениях ингредиентов и ошибка в случае, если ингредиенты не указаны или указаны некорректно")
    public void createOrderWithAuthorization() {
        withoutIngredients();
        withIngredients();
        incorrectIngredients();
    }

    @Step("Создание с ингредиентам - успех")
    private void withIngredients() {
        order.successCreateOrderWithAuthorization(userToken, correctIngredients);
    }

    @Step("Создание без ингредиентов - ошибка")
    private void withoutIngredients() {
        incorrectIngredients = new ObjectsIngredients(List.of());
        String error = "Ingredient ids must be provided";

        order.errorCreateOrder(userToken, incorrectIngredients, error);
    }

    @Step("Создание с некорректными ингредиентами - ошибка")
    private void incorrectIngredients() {
        incorrectIngredients = new ObjectsIngredients(List.of("1110c5a71d1f82001bdaa111"));
        String error = "One or more ids provided are incorrect";

        order.errorCreateOrder(userToken, incorrectIngredients, error);
    }

    // ----------------------------------------------------

    // Создание заказа без авториизации

    @Test
    @DisplayName("Успешное создание заказа без авторизации")
    @Description("Успешное сооздание заказа при корретных данных без авторизации пользователя")
    public void createOrderWithoutAuthorization() {
        order.successCreateOrderWithoutAuthorization(correctIngredients);
    }

    @AfterEach
    public void deleteUser(){
        register.deleteUser(userToken);
    }

}
