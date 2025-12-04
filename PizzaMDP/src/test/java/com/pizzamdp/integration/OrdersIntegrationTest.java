package com.pizzamdp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class OrdersIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USUARIO", "ADMINISTRADOR"})
    void testCreateAndGetOrder() throws Exception {
        // Test data
        String orderJson = "{ \"nombreCliente\": \"Integration Test\", \"direccionCliente\": \"Test Address\", \"estadoOrden\": \"PENDIENTE\" }";

        // Create order
        mockMvc.perform(post("/oms/ordenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.nombreCliente", equalTo("Integration Test")));

        // Verify it was saved
        mockMvc.perform(get("/oms/ordenes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$[0].nombreCliente", equalTo("Integration Test")));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USUARIO", "ADMINISTRADOR"})
    void testLoadTestCreateOrders() throws Exception {
        String orderJson = "{ \"nombreCliente\": \"Load Test\", \"direccionCliente\": \"Test Address\", \"estadoOrden\": \"PENDIENTE\" }";
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < 100; i++) {
            mockMvc.perform(post("/oms/ordenes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(orderJson))
                    .andExpect(status().isOk());
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Assert that the operation completes within 5 seconds (a reasonable threshold)
        assert(duration < 5000);
    }
}
