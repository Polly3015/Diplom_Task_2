package tests;

import objects.ObjectsUser;
import requests.Login;
import requests.RegisterAndDelete;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LoginTest {

    ObjectsUser user = new ObjectsUser("test130@test.ru", "123456", "Polly");
    ObjectsUser bodyIncorrectLogin = new ObjectsUser("test139@test.ru", "123456");
    ObjectsUser bodyIncorrectPassword = new ObjectsUser("test130@test.ru", "111111");
    ObjectsUser bodyCorrectLoginAndPassword = new ObjectsUser("test130@test.ru", "123456");

    RegisterAndDelete register = new RegisterAndDelete();
    Login login = new Login();
    private String userToken = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru/";

        userToken = register.successRegisterUser(user);
    }

    // Неверный логин и пароль
    @Test
    @DisplayName("Авторизация с неверными данными")
    @Description("Невозможно авторизоваться с неверными данными - падает ошибка")
    public void loginUserErrorIncorrectData() {
        sendLoginRequestWithoutIncorrectLogin();
        sendLoginRequestWithoutIncorrectPassword();
    }

        @Step("Авторизация с неправильным логином")
        private void sendLoginRequestWithoutIncorrectLogin() {
            login.loginError401(bodyIncorrectLogin);
        }

        @Step("Авторизация с неправильным паролем")
        private void sendLoginRequestWithoutIncorrectPassword() {
            login.loginError401(bodyIncorrectPassword);
        }

    // ----------------------------------------------------

    @Test
    @DisplayName("Успешная авторизация пользователя")
    @Description("Успешная авторизация пользователя при корретных данных")
    public void loginCourierSuccess() {
        login.successLogin(bodyCorrectLoginAndPassword);
    }

    @AfterEach
    public void deleteUser(){
        register.deleteUser(userToken);
    }

}
