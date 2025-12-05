package com.pizzamdp.services;

import com.pizzamdp.entities.Cliente;
import com.pizzamdp.entities.Persona;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonaService {

    @Autowired
    private PersonaRepository personaRepository;

    public Cliente findOrCreateCliente(User user) {
        return personaRepository.findByUser(user)
                .map(persona -> {
                    if (persona instanceof Cliente) {
                        return (Cliente) persona;
                    } else {
                        // Handle the case where a User is associated with a different Persona type (e.g., Rider)
                        throw new IllegalStateException("User is not a Cliente");
                    }
                })
                .orElseGet(() -> {
                    Cliente newCliente = new Cliente();
                    newCliente.setUser(user);
                    // Set other default properties for a new Cliente if necessary
                    return personaRepository.save(newCliente);
                });
    }
}
