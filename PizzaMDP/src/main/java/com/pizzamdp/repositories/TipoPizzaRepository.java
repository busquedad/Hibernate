package com.pizzamdp.repositories;

import com.pizzamdp.entities.TipoPizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoPizzaRepository extends JpaRepository<TipoPizza, Integer> {
}
