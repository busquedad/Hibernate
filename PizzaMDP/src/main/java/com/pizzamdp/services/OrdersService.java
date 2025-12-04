package com.pizzamdp.services;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class OrdersService {

    @Autowired
    private OrdenRepository ordenRepository;

    @Transactional(readOnly = true)
    public List<Orden> getOrdersForUser(User user) {
        boolean isCliente = user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .anyMatch("ROLE_CLIENTE"::equals);

        boolean isRider = user.getAuthorities().stream()
                              .map(GrantedAuthority::getAuthority)
                              .anyMatch("ROLE_RIDER"::equals);

        if (isCliente) {
            return ordenRepository.findByCliente(user);
        } else if (isRider) {
            return ordenRepository.findByRider(user);
        }

        // Por defecto (o para otros roles como ADMIN), devolver todo.
        // En un sistema real, esto podría requerir un rol explícito.
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
