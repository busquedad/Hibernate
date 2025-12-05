package com.pizzamdp.repositories;

import com.pizzamdp.entities.Persona;
import com.pizzamdp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByUser(User user);
}
