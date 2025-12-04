package com.pizzamdp.controllers;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.services.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/oms/ordenes")
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public List<Orden> getAllOrders() {
        return ordersService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrderById(@PathVariable Integer id) {
        return ordersService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Orden createOrder(@RequestBody Orden orden) {
        return ordersService.createOrder(orden);
    }
}
