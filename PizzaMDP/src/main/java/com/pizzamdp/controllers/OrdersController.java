package com.pizzamdp.controllers;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import com.pizzamdp.security.CustomOidcUser;
import com.pizzamdp.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oms/ordenes")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public List<Orden> getAllOrders(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return ordersService.getOrdersForUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrderById(@PathVariable Integer id) {
        // En un sistema completo, este endpoint también debería validar
        // que el cliente/rider solo pueda ver sus propias órdenes.
        return ordersService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Orden createOrder(@RequestBody Orden orden, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        orden.setCliente(user);
        return ordersService.createOrder(orden);
    }

    private User getUserFromAuthentication(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            return (User) principal;
        }
        if (principal instanceof CustomOidcUser) {
            return ((CustomOidcUser) principal).getUser();
        }
        // Debería lanzar una excepción si el tipo de principal es inesperado
        throw new IllegalStateException("Unknown principal type: " + principal.getClass());
    }
}
