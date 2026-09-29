import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

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
    }


    // Создание заказов с авторизациией
    @Test
    public void createOrderWithAuthorization() {
        withoutIngredients();
        withIngredients();
        incorrectIngredients();
    }

    @Step("Создание без ингредиентов - ошибка")
    private void withoutIngredients() {
        String bodyIngredients = "{}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyIngredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400)
                .body("message", is("Ingredient ids must be provided"));
    }

    @Step("Создание с ингредиентам")
    private void withIngredients() {
        String bodyIngredients = "{\"ingredients\": [\"" + ingredients + "\"]}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyIngredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.ingredients", notNullValue())
                .body("order._id", notNullValue());
    }

    @Step("Создание с некорректными ингредиентами - ошибка")
    private void incorrectIngredients() {
        String bodyIngredients = "{\"ingredients\": [\"1110c5a71d1f82001bdaa111\"]}";
        given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(bodyIngredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400)
                .body("success", is(false))
                .body("message", is("One or more ids provided are incorrect"));
    }

    // ----------------------------------------------------

    // Создание заказа без авториизации

    @Test
    @Step("Создание заказа без авторизации")
    public void createOrderWithoutAuthorization() {

        String bodyIngredients = "{\"ingredients\": [\"" + ingredients + "\"]}";
        given()
                .header("Content-type", "application/json")
                .body(bodyIngredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue());
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
