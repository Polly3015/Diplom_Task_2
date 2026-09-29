import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class LoginTest {

    User user = new User("test130@test.ru", "123456", "Polly");
    private String userToken = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";

        userToken =
                given()
                        .header("Content-type", "application/json")
                        .body(user)
                        .when()
                        .post("/api/auth/register")
                        .then()
                        .statusCode(200)
                        .body("success", is(true))
                        .extract()
                        .path("accessToken");
    }

    // Неверный логин и пароль
    @Test
    public void loginUserErrorIncorrectData() {
        sendLoginRequestWithoutIncorrectLogin();
        sendLoginRequestWithoutIncorrectPassword();
    }

    @Step("Авторизация с неправильным логином - ошибка 401")
    private void sendLoginRequestWithoutIncorrectLogin() {
        String bodyIncorrectLogin = "{\"email\":\"test139@test.ru\", \"password\":\"123456\"}";
        given()
                .header("Content-type", "application/json")
                .body(bodyIncorrectLogin)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("message", is("email or password are incorrect"));
    }

    @Step("Авторизация с неправильным паролем - ошибка 401")
    private void sendLoginRequestWithoutIncorrectPassword() {
        String bodyIncorrectPassword = "{\"email\":\"test130@test.ru\", \"password\":\"111111\"}";
        given()
                .header("Content-type", "application/json")
                .body(bodyIncorrectPassword)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("message", is("email or password are incorrect"));
    }

    // ----------------------------------------------------

    @Test
    @Step("Успешная авторизация пользователя")
    public void loginCourierSuccess() {
        String bodyCorrectLoginAndPassword = "{\"email\":\"test130@test.ru\", \"password\":\"123456\"}";
        given()
                .header("Content-type", "application/json")
                .body(bodyCorrectLoginAndPassword)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @AfterEach
    public void deleteUser(){

            given()
                    .header("Content-type", "application/json")
                    .header("Authorization", userToken)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(202);
        }

}
