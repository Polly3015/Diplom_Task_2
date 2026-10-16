package requests;

import objects.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class Login {

    public String successLogin(User login) {
        return given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("success", is(true)).toString();
    }

    public String loginError401(User login) {
        return given()
                .header("Content-type", "application/json")
                .body(login)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401)
                .body("message", is("email or password are incorrect")).toString();
    }

}
