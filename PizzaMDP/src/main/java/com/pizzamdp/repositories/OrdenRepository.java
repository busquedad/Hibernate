package com.pizzamdp.repositories;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {
    List<Orden> findByCliente(User cliente);
    List<Orden> findByRider(User rider);
}
