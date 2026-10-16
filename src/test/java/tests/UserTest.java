package tests;

import objects.User;
import requests.RegisterAndDelete;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest extends BaseApiTest {

    User user = new User("test130@test.ru", "123456", "Polly");
    User userWithoutName = new User("test130@test.ru", "123456");
    private String userToken = null;

    RegisterAndDelete register = new RegisterAndDelete();

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

