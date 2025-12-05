package com.pizzamdp.controllers;

import com.pizzamdp.config.AuthorizationServerConfig;
import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.*;
import com.pizzamdp.repositories.OrdenRepository;
import com.pizzamdp.repositories.PersonaRepository;
import com.pizzamdp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestEntityManager
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = AuthorizationServerConfig.class))
public class OrdersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private PersonaRepository personaRepository;

    @Autowired
    private TestEntityManager entityManager;

    @MockBean
    private JwtDecoder jwtDecoder;

    private User clienteUser;
    private User riderUser;
    private Cliente cliente;
    private Rider rider;
    private Local local;

    @BeforeEach
    void setUp() {
        ordenRepository.deleteAll();
        personaRepository.deleteAll();
        userRepository.deleteAll();

        local = new Local(null, "Test Local", "123 Main St", null, null, "A test local", EstadoLocal.ABIERTO);
        entityManager.persist(local);

        clienteUser = User.builder().username("cliente@test.com").provider(Provider.LOCAL).roles(Collections.singletonList("ROLE_CLIENTE")).password("password").build();
        userRepository.save(clienteUser);
        cliente = new Cliente();
        cliente.setUser(clienteUser);
        personaRepository.save(cliente);

        riderUser = User.builder().username("rider@test.com").provider(Provider.LOCAL).roles(Collections.singletonList("ROLE_RIDER")).password("password").build();
        userRepository.save(riderUser);
        rider = new Rider();
        rider.setUser(riderUser);
        rider.setEstado(EstadoRider.DISPONIBLE);
        personaRepository.save(rider);

        Orden ordenCliente1 = new Orden(null, local, cliente, LocalDateTime.now(), TipoOrden.DELIVERY, EstadoOrden.PENDIENTE, null, null, null, "456 Side St", null);
        Orden ordenCliente2 = new Orden(null, local, cliente, LocalDateTime.now(), TipoOrden.DELIVERY, EstadoOrden.EN_CAMINO, null, null, rider, "456 Side St", null);
        ordenRepository.saveAll(List.of(ordenCliente1, ordenCliente2));
    }

    @Test
    void whenAuthenticatedAsCliente_shouldReturnOnlyTheirOrders() throws Exception {
        mockMvc.perform(get("/oms/ordenes")
                        .with(jwt().jwt(j -> j.subject(clienteUser.getUsername())).authorities(new SimpleGrantedAuthority("ROLE_CLIENTE"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void whenAuthenticatedAsRider_shouldReturnOnlyAssignedOrders() throws Exception {
        mockMvc.perform(get("/oms/ordenes")
                        .with(jwt().jwt(j -> j.subject(riderUser.getUsername())).authorities(new SimpleGrantedAuthority("ROLE_RIDER"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].estadoOrden").value("EN_CAMINO"));
    }

    @Test
    void whenUnauthenticated_shouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/oms/ordenes"))
                .andExpect(status().isUnauthorized());
    }
}
