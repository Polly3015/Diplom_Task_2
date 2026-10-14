package tests;

import objects.ObjectsUser;
import requests.RegisterAndDelete;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

    ObjectsUser user = new ObjectsUser("test130@test.ru", "123456", "Polly");
    ObjectsUser userWithoutName = new ObjectsUser("test130@test.ru", "123456");
    private String userToken = null;

    RegisterAndDelete register = new RegisterAndDelete();

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru/";
    }


    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("Создание пользователя без поля name невозможно - падает ошибка")
    public void testSendCreateRequestWithoutName() {
        register.registerUserError403(userWithoutName, "Email, password and name are required fields");
    }


    @Test
    @DisplayName("Успешное создание пользователя")
    @Description("Успешное создание пользователя с корректными данными")
    public void testCreateUserSuccess() {
        userToken = register.successRegisterUser(user);
    }


    @Test
    @DisplayName("Повторное создание пользователя")
    @Description("Повторное создание пользователя с одинаковой почтой невозможно - падает ошибкам")
    public void testErrorCreateSameCourier() {
        // Успешная регистрация
        userToken = register.successRegisterUser(user);
        // Повторая регистрация с тем же пользователем
        register.registerUserError403(user, "User already exists");
    }

    @AfterEach
    public void deleteUser() {
        if (userToken != null) {
            register.deleteUser(userToken);
        }
    }

}

