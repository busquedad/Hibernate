package com.pizzamdp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzamdp.config.AuthorizationServerConfig;
import com.pizzamdp.entities.Provider;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@Disabled("Disabled due to persistent ApplicationContext loading conflicts and Docker environment issues.")
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthorizationServerConfig.class))
public class OrdersIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private JwtDecoder jwtDecoder;

    private User cliente;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        cliente = User.builder()
                .username("cliente-integration@test.com")
                .provider(Provider.LOCAL)
                .roles(Collections.singletonList("CLIENTE"))
                .password("password")
                .build();
        userRepository.save(cliente);
    }

    @Test
    void testCreateOrderAuthenticated() throws Exception {
        String orderJson = "{ \"estadoOrden\": \"PENDIENTE\" }";

        mockMvc.perform(post("/oms/ordenes")
                        .with(jwt().jwt(j -> j.subject(cliente.getUsername())).authorities(cliente.getAuthorities().toArray(new GrantedAuthority[0])))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.cliente.username", equalTo(cliente.getUsername())));
    }
}
