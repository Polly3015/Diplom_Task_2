package requests;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;

public class ReceiveIngredient {

    public String oneIngredientId;

    public ReceiveIngredient() {
    }

    public String searchIngredient() {

        RestAssured.baseURI ="https://qa-stellarburgers.education-services.ru/";

        oneIngredientId =
                given()
                        .header("Content-type", "application/json")
                        .when()
                        .get("/api/ingredients")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("data[0]._id");

        return oneIngredientId;
    }

}
