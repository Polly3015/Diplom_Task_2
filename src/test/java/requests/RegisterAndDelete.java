package requests;

import objects.ObjectsUser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class RegisterAndDelete {

    // Успешная регистрация пользователя + получение токена
    public String successRegisterUser(ObjectsUser user) {
        return given()
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


    // Неудачная попытка регистрации, ошибка 403
    public String registerUserError403(ObjectsUser user, String error) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(403)
                .body("message", is(error)).toString();
    }

    // Удаление пользователя
    public void deleteUser(String token) {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user")
                .then()
                .statusCode(202);
    }
}
