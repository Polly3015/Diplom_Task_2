package requests;

import objects.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class ChangeUser {

    // Успешное изменение данных пользователя
    public String successChange(String userToken, User newBody, String bodyAnswer) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", userToken)
                .body(newBody)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .extract()
                .path(bodyAnswer);
    }

    // Изменение данных пользователя без авторизации - ошибка
    public String errorChangeWithoutAuthorization(User newBody) {
        return given()
                .header("Content-type", "application/json")
                .body(newBody)
                .when()
                .patch("/api/auth/user")
                .then()
                .statusCode(401)
                .body("message", is("You should be authorised")).toString();
    }

}
