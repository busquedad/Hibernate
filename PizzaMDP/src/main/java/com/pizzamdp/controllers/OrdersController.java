package com.pizzamdp.controllers;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import com.pizzamdp.messaging.config.RabbitConfig;
import com.pizzamdp.entities.Cliente;
import com.pizzamdp.messaging.dto.OrderCreateEvent;
import com.pizzamdp.security.oidc.CustomOidcUser;
import com.pizzamdp.services.OrdersService;
import com.pizzamdp.services.PersonaService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private com.pizzamdp.repositories.UserRepository userRepository;

    @GetMapping
    public List<Orden> getAllOrders(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return ordersService.getOrdersForUser(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> getOrderById(@PathVariable Long id) {
        // En un sistema completo, este endpoint también debería validar
        // que el cliente/rider solo pueda ver sus propias órdenes.
        return ordersService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> createOrder(@RequestBody Orden orden, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        Cliente cliente = personaService.findOrCreateCliente(user);
        orden.setCliente(cliente);

        // Aquí, en un sistema real, se realizaría una validación exhaustiva del objeto Orden
        // antes de encolar el evento.

        OrderCreateEvent event = new OrderCreateEvent(orden);
        rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, event);

        return ResponseEntity.accepted().build();
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
        if (principal instanceof org.springframework.security.oauth2.jwt.Jwt) {
            String username = ((org.springframework.security.oauth2.jwt.Jwt) principal).getSubject();
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new IllegalStateException("User not found in database"));
        }
        // Debería lanzar una excepción si el tipo de principal es inesperado
        throw new IllegalStateException("Unknown principal type: " + principal.getClass());
    }
}
