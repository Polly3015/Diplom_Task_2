package requests;

import objects.Ingredients;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class Order {

    // Создание заказа

    public String successCreateOrderWithAuthorization(String userToken, Ingredients ingredients) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.ingredients", notNullValue())
                .body("order._id", notNullValue()).toString();
    }

    public String successCreateOrderWithoutAuthorization(Ingredients ingredients) {
        return given()
                .header("Content-type", "application/json")
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("order.number", notNullValue()).toString();
    }

    public String errorCreateOrder(String userToken, Ingredients ingredients, String error) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then()
                .statusCode(400)
                .body("message", is(error)).toString();
    }

    // Запрос заказов

    public String successGetOrderWithAuthorization(String userToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .when()
                .get("/api/orders")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue()).toString();
    }

    public String errorGetOrderWithoutAuthorization() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised")).toString();
    }





}
