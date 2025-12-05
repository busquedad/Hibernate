package com.pizzamdp.services;

import com.pizzamdp.entities.*;
import com.pizzamdp.repositories.OrdenRepository;
import com.pizzamdp.repositories.PersonaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OrdersServiceTest {

    @Mock
    private OrdenRepository ordenRepository;

    @Mock
    private PersonaRepository personaRepository;

    @InjectMocks
    private OrdersService ordersService;

    private User clienteUser;
    private User riderUser;
    private Cliente cliente;
    private Rider rider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteUser = User.builder().id(1L).username("cliente@test.com").provider(Provider.LOCAL).roles(Collections.singletonList("CLIENTE")).build();
        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setUser(clienteUser);

        riderUser = User.builder().id(2L).username("rider@test.com").provider(Provider.LOCAL).roles(Collections.singletonList("RIDER")).build();
        rider = new Rider();
        rider.setId(2L);
        rider.setUser(riderUser);
    }

    @Test
    void testCreateOrder() {
        Orden orden = new Orden();
        orden.setCliente(cliente);
        orden.setEstadoOrden(EstadoOrden.PENDIENTE);

        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden result = ordersService.createOrder(orden);

        assertEquals(cliente, result.getCliente());
    }

    @Test
    void testGetOrdersForUser_whenUserIsCliente() {
        Orden orden = new Orden();
        orden.setCliente(cliente);
        when(personaRepository.findByUser(clienteUser)).thenReturn(Optional.of(cliente));
        when(ordenRepository.findByCliente(cliente)).thenReturn(Collections.singletonList(orden));

        List<Orden> result = ordersService.getOrdersForUser(clienteUser);

        assertEquals(1, result.size());
        assertEquals(cliente, result.get(0).getCliente());
    }

    @Test
    void testGetOrdersForUser_whenUserIsRider() {
        Orden orden = new Orden();
        orden.setRider(rider);
        when(personaRepository.findByUser(riderUser)).thenReturn(Optional.of(rider));
        when(ordenRepository.findByRider(rider)).thenReturn(Collections.singletonList(orden));

        List<Orden> result = ordersService.getOrdersForUser(riderUser);

        assertEquals(1, result.size());
        assertEquals(rider, result.get(0).getRider());
    }
}
