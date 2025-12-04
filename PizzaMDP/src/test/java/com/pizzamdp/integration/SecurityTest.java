package com.pizzamdp.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
public class SecurityTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAccessToProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/oms/ordenes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = {"INVALID_ROLE"})
    void testAccessToProtectedEndpointWithWrongRole() throws Exception {
        mockMvc.perform(get("/oms/ordenes"))
                .andExpect(status().isForbidden());
    }
}
