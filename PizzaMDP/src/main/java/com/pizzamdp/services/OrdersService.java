package com.pizzamdp.services;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Transactional(readOnly = true)
    public List<Orden> getAllOrders() {
        return ordenRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Orden> getOrderById(Integer id) {
        return ordenRepository.findById(id);
    }

    @Transactional
    public Orden createOrder(Orden orden) {
        orden.setFechaOrden(LocalDateTime.now());
        // En un caso real, aquí se validaría el estado, calcularía el total, etc.
        return ordenRepository.save(orden);
    }
}
