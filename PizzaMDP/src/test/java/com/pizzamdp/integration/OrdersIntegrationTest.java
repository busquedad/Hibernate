package com.pizzamdp.integration;

import com.pizzamdp.entities.Orden;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.test.context.support.WithMockUser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@ActiveProfiles("test")
public class OrdersIntegrationTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    void testCreateAndGetOrder() {
        // Test data
        String orderJson = "{ \"nombreCliente\": \"Integration Test\", \"direccionCliente\": \"Test Address\", \"estadoOrden\": \"PENDIENTE\" }";

        // Create order
        given()
            .contentType(ContentType.JSON)
            .body(orderJson)
        .when()
            .post("/oms/ordenes")
        .then()
            .statusCode(200)
            .body("id", notNullValue())
            .body("nombreCliente", equalTo("Integration Test"));

        // Verify it was saved
        given()
        .when()
            .get("/oms/ordenes")
        .then()
            .statusCode(200)
            .body("size()", equalTo(1))
            .body("[0].nombreCliente", equalTo("Integration Test"));
    }

    @Test
    @WithMockUser(username="user", roles={"USER"})
    void testLoadTestCreateOrders() {
        String orderJson = "{ \"nombreCliente\": \"Load Test\", \"direccionCliente\": \"Test Address\", \"estadoOrden\": \"PENDIENTE\" }";
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            given()
                .contentType(ContentType.JSON)
                .body(orderJson)
            .when()
                .post("/oms/ordenes")
            .then()
                .statusCode(200);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Assert that the operation completes within 5 seconds (a reasonable threshold)
        assert(duration < 5000);
    }
}
