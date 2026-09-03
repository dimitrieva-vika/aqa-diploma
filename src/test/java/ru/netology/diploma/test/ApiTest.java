package ru.netology.diploma.test;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class ApiTest {

    @BeforeAll
    static void setUpApi() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8080;
    }

    @Test
    @DisplayName("35. POST /payment с пустым телом возвращает 400 (БАГ: возвращает 500)")
    void shouldReturn400ForEmptyPaymentRequest() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("error", containsString("Bad Request"));
    }

    @Test
    @DisplayName("36. POST /credit с пустым телом возвращает 400 (БАГ: возвращает 500)")
    void shouldReturn400ForEmptyCreditRequest() {
        given()
                .contentType("application/json")
                .body("{}")
                .when()
                .post("/api/v1/credit")
                .then()
                .statusCode(400)
                .body("status", is(400))
                .body("error", containsString("Bad Request"));
    }

    @Test
    @DisplayName("37. POST /payment с невалидной картой возвращает DECLINED")
    void shouldReturnDeclinedForInvalidCard() {
        String json = "{\"number\":\"1111 1111 1111 1111\",\"month\":\"08\",\"year\":\"26\",\"holder\":\"Ivan Petrov\",\"cvc\":\"123\"}";

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/api/v1/pay")
                .then()
                .statusCode(200)
                .body("status", is("DECLINED"));
    }

    @Test
    @DisplayName("38. GET /invalid возвращает 404")
    void shouldReturn404ForInvalidEndpoint() {
        given()
                .when()
                .get("/api/v1/invalid")
                .then()
                .statusCode(404)
                .body("status", is(404))
                .body("error", containsString("Not Found"));
    }
}