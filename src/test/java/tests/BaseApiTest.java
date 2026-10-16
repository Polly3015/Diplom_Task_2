package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseApiTest {

    @BeforeAll
    static void setUpBase() {
        RestAssured.baseURI = "https://qa-stellarburgers.education-services.ru";
    }

}
