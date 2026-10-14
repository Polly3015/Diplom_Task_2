package tests;

import objects.ObjectsUser;
import requests.ChangeUser;
import requests.RegisterAndDelete;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeUserTest {

    ObjectsUser user = new ObjectsUser("test130@test.ru", "123456", "Polly");
    ObjectsUser bodyNewLogin = new ObjectsUser();
    ObjectsUser bodyNewPassword = new ObjectsUser();
    ObjectsUser bodyNewName = new ObjectsUser();

    RegisterAndDelete register = new RegisterAndDelete();
    ChangeUser changeUser = new ChangeUser();

    private String userToken = null;
    private String newData = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru/";

        userToken = register.successRegisterUser(user);
    }

    // Попытка изменений без авторизациии
    @Test
    @DisplayName("Изменение данных без авторизации")
    @Description("Нельзя изменить данные без авторизации - падает ошибка")
    public void changeUserWithoutAuthorization() {
        bodyNewLogin.setEmail("test131@test.ru");
        changeUser.errorChangeWithoutAuthorization(bodyNewLogin);
    }

    // ----------------------------------------------------

    // Успешное изменение всех данных
    @Test
    @DisplayName("Успешное изменение данных пользователя")
    @Description("Успешное изменениие данных у авторизованного пользователя")
    public void changeUserSuccess() {
        changeUserLogin();
        changeUserPassword();
        changeUserName();
    }

    @Step("Изменение логина")
    public void changeUserLogin() {

        String email = "test131@test.ru";
        bodyNewLogin.setEmail(email);

        newData = changeUser.successChange(userToken, bodyNewLogin, "user.email");
        assertEquals(email, newData);
    }

    @Step("Иизменение пароля")
    public void changeUserPassword() {

        String password = "111111";
        bodyNewPassword.setPassword(password);
        changeUser.successChange(userToken, bodyNewPassword, "user.email");
    }

    @Step("Изменение имени")
    public void changeUserName() {

        String name = "Kate";
        bodyNewName.setName(name);

        newData = changeUser.successChange(userToken, bodyNewName, "user.name");
        assertEquals(name, newData);
    }

    @AfterEach
    public void deleteUser(){
        register.deleteUser(userToken);
    }

}
