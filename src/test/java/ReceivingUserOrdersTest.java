import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReceivingUserOrdersTest {

    User user = new User("test130@test.ru", "123456", "Polly");
    private String userToken = null;

    String ingredients = "61c0c5a71d1f82001bdaaa6d";

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

        String bodyIngredients = "{\"ingredients\": [\"" + ingredients + "\"]}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyIngredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void receiveOrders() {
        withoutAuthorization();
        withAuthorization();
    }

    @Step("Запрос без авторизации - ошибка")
    private void withoutAuthorization() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Step("Запрос c авторизацией")
    private void withAuthorization() {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
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
