package com.pizzamdp.repositories;

import com.pizzamdp.entities.TamanioPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TamanioPizzaRepository extends JpaRepository<TamanioPizza, Integer> {
}
