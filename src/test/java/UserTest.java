import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserTest {

    User user = new User("test130@test.ru", "123456", "Polly");
    private String userToken = null;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.education-services.ru/";
    }

    // ----------------------------------------------------

    // Если одного из полей нет, запрос возвращает ошибку;
    @Test
    @Step("Создание пользователя без поля name невозможно - ошибка 403")
    public void testSendCreateRequestWithoutName() {
        String bodyWithoutName = "{\"email\":\"test130@test.ru\",\"password\":\"123456\"}";
        given()
                .header("Content-type", "application/json")
                .body(bodyWithoutName)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    // ----------------------------------------------------

    @Test
    @Step("Успешное создание пользователя")
    public void testCreateUserSuccess() {

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

    // ----------------------------------------------------

    // Нельзя создать двух одинаковых пользователей;
    @Test
    @Step("Повторное создание пользователя с одинаковой почтой неовзможно - ошибка 403")
    public void testErrorCreateSameCourier() {

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

        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("message", is("User already exists"));
    }


    @AfterEach
    public void deleteUser(){

        if (userToken == null) {
            return; // Если нет токена, то выходим из метода и не удаляем пользователя
        } else {
            // Если есть токен, то удаляем пользователя
            given()
                    .header("Content-type", "application/json")
                    .header("Authorization", userToken)
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .statusCode(202);
        }
    }

}

