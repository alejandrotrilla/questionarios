package ar.com.trilla.questionarios;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;

@QuarkusTest
public class CustomerResourceTest {


    @Test
    public void testCreateEndpoint() {
        given()
                .when()
                .body("{\"name\" : \"Red Customer\"}")
                .contentType("application/json")
                .post("/customers")
                .then()
                .statusCode(201);
        given()
                .when()
                .body("{\"name\" : \"Blue Customer\"}")
                .contentType("application/json")
                .post("/customers")
                .then()
                .statusCode(201);
        given()
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body(containsString("Blue Customer"));
        given()
                .when()
                .get("/customers/1")
                .then()
                .statusCode(200)
                .body(containsString("Red Customer"));
        given()
                .when()
                .get("/customers/999")
                .then()
                .statusCode(204)
                .body(emptyString());
    }

    @Test
    public void testGetAllEndpoint() {
        given()
                .when()
                .get("/customers")
                .then()
                .statusCode(200)
                .body(is("[]"));
    }

}