package com.pizzamdp.services;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.repositories.OrdenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        Orden orden = new Orden();
        orden.setNombreCliente("Juan Perez");
        orden.setDireccionCliente("Calle Falsa 123");
        orden.setEstadoOrden("PENDIENTE");

        when(ordenRepository.save(any(Orden.class))).thenReturn(orden);

        Orden result = ordersService.createOrder(orden);

        assertEquals("Juan Perez", result.getNombreCliente());
    }

    @Test
    void testGetAllOrders() {
        Orden orden = new Orden(1, "Juan Perez", "Calle Falsa 123", LocalDateTime.now(), "PENDIENTE", null);
        when(ordenRepository.findAll()).thenReturn(Collections.singletonList(orden));

        List<Orden> result = ordersService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals("Juan Perez", result.get(0).getNombreCliente());
    }
}
