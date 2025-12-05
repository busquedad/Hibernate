package com.pizzamdp.services;

import com.pizzamdp.entities.*;
import com.pizzamdp.repositories.OrdenRepository;
import com.pizzamdp.repositories.PersonaRepository;
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

    @Autowired
    private PersonaRepository personaRepository;

    @Transactional(readOnly = true)
    public List<Orden> getOrdersForUser(User user) {
        Optional<Persona> personaOpt = personaRepository.findByUser(user);
        if (personaOpt.isEmpty()) {
            return Collections.emptyList();
        }

        Persona persona = personaOpt.get();
        if (persona instanceof Cliente) {
            return ordenRepository.findByCliente((Cliente) persona);
        } else if (persona instanceof Rider) {
            return ordenRepository.findByRider((Rider) persona);
        } else if (persona instanceof Staff) {
            // Assuming Staff with ADMIN role can see all orders in their local
            Staff staff = (Staff) persona;
            if (staff.getRolOperativo() == RolStaff.ADMIN) {
                return ordenRepository.findByLocal(staff.getLocal());
            }
        }

        // Default to empty list for security
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public Optional<Orden> getOrderById(Long id) {
        return ordenRepository.findById(id);
    }

    @Transactional
    public Orden createOrder(Orden orden) {
        orden.setFechaOrden(LocalDateTime.now());
        // En un caso real, aquí se validaría el estado, calcularía el total, etc.
        return ordenRepository.save(orden);
    }
}
