package com.pizzamdp.integration;

import com.pizzamdp.config.AuthorizationServerConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Disabled("Disabled due to Docker environment issues and ApplicationContext conflicts.")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthorizationServerConfig.class))
public class SecurityTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void testAccessToProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/oms/ordenes"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testAccessWithValidJwtButNoMatchingUser() throws Exception {
        // The JWT is valid (mocked decoder), but JpaUserDetailsService won't find the user "nonexistent"
        // This should result in an unauthorized error at the authentication stage.
        mockMvc.perform(get("/oms/ordenes").with(jwt().jwt(j -> j.subject("nonexistent"))))
                .andExpect(status().isUnauthorized());
    }
}
