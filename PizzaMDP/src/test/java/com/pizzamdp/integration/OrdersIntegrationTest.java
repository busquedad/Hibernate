package com.pizzamdp.integration;

import com.pizzamdp.config.AuthorizationServerConfig;
import com.pizzamdp.entities.Provider;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.OrdenRepository;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthorizationServerConfig.class))
@Disabled("This test requires a running Docker environment for Testcontainers (RabbitMQ) and is disabled in environments where Docker is not available.")
public class OrdersIntegrationTest extends AbstractIntegrationTest {

    @Container
    static final RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13-management-alpine");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMQContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMQContainer::getAdminPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    private User cliente;

    @BeforeEach
    void setUp() {
        ordenRepository.deleteAll();
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
    void testCreateOrderAsynchronously() throws Exception {
        String orderJson = "{ \"estadoOrden\": \"PENDIENTE\" }";

        mockMvc.perform(post("/oms/ordenes")
                        .with(jwt().jwt(j -> j.subject(cliente.getUsername())).authorities(cliente.getAuthorities().toArray(new GrantedAuthority[0])))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(status().isAccepted());

        await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
            List<com.pizzamdp.entities.Orden> orders = ordenRepository.findByCliente(cliente);
            assertThat(orders).hasSize(1);
            assertThat(orders.get(0).getCliente().getUsername()).isEqualTo(cliente.getUsername());
            assertThat(orders.get(0).getEstadoOrden()).isEqualTo("PENDIENTE");
        });
    }
}
