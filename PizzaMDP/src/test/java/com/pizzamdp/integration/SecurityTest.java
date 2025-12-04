package com.pizzamdp.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
public class SecurityTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void testAccessToProtectedEndpointWithoutToken() {
        given()
        .when()
            .get("/oms/ordenes")
        .then()
            .statusCode(401);
    }

    @Test
    @WithMockUser(username="user", roles={"INVALID_ROLE"})
    void testAccessToProtectedEndpointWithWrongRole() {
        given()
        .when()
            .get("/oms/ordenes")
        .then()
            .statusCode(403);
    }
}
