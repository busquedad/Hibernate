package com.pizzamdp.repositories;

import com.pizzamdp.entities.VariedadPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VariedadPizzaRepository extends JpaRepository<VariedadPizza, Integer> {
}
