import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ChangeUserTest {

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

    // Попытка изменений без авторизациии
    @Test
    @Step("Изменение данных без авторизации невозможно - ошибка 401")
    public void changeUserWithoutAuthorization() {

        String bodyNewLogin = "{\"email\":\"test131@test.ru\"}";
        given()
                .header("Content-type", "application/json")
                .body(bodyNewLogin)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }

    // ----------------------------------------------------

    // Успешное изменение всех данных
    @Test
    public void changeUserSuccess() {
        changeUserLogin();
        changeUserPassword();
        changeUserName();
    }

    @Step("Успешное изменение логина")
    public void changeUserLogin() {

        String email = "test131@test.ru";
        String bodyNewLogin = "{\"email\":\"" + email + "\"}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyNewLogin)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.email", is(email));
    }

    @Step("Успешное изменение пароля")
    public void changeUserPassword() {

        String password = "111111";
        String bodyNewPassword = "{\"password\":\"" + password + "\"}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyNewPassword)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Успешное изменение имени")
    public void changeUserName() {

        String name = "Kate";
        String bodyNewName = "{\"name\":\"" + name + "\"}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyNewName)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("user.name", is(name));
    }

    @AfterEach
    public void deleteUser(){

        given()
                .header("Authorization", userToken)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }

}
