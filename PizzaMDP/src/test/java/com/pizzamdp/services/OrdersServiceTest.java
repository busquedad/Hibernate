package com.pizzamdp.services;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.Provider;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrdersServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @InjectMocks
    private OrdersService ordersService;

    private User cliente;
    private User rider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cliente = User.builder()
                .id(1L)
                .username("cliente@test.com")
                .provider(Provider.LOCAL)
                .roles(Collections.singletonList("CLIENTE"))
                .build();
        rider = User.builder()
                .id(2L)
                .username("rider@test.com")
                .provider(Provider.LOCAL)
                .roles(Collections.singletonList("RIDER"))
                .build();
    }

    @Test
    void testCreateOrder() {
        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setEstadoOrden("PENDIENTE");

        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden result = ordersService.createOrder(orden);

        assertEquals("cliente@test.com", result.getCliente().getUsername());
    }

    @Test
    void testGetOrdersForUser_whenUserIsCliente() {
        Orden orden = new Orden(1, cliente, null, LocalDateTime.now(), "PENDIENTE", null);
        when(ordenRepository.findByCliente(cliente)).thenReturn(Collections.singletonList(orden));

        List<Orden> result = ordersService.getOrdersForUser(cliente);

        assertEquals(1, result.size());
        assertEquals("cliente@test.com", result.get(0).getCliente().getUsername());
    }

    @Test
    void testGetOrdersForUser_whenUserIsRider() {
        Orden orden = new Orden(2, cliente, rider, LocalDateTime.now(), "ASIGNADA", null);
        when(ordenRepository.findByRider(rider)).thenReturn(Collections.singletonList(orden));

        List<Orden> result = ordersService.getOrdersForUser(rider);

        assertEquals(1, result.size());
        assertEquals("rider@test.com", result.get(0).getRider().getUsername());
    }
}
