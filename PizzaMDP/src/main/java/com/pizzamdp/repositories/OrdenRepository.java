package com.pizzamdp.repositories;

import com.pizzamdp.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdenRepository extends JpaRepository<Orden, Long> {
    List<Orden> findByCliente(Cliente cliente);
    List<Orden> findByRider(Rider rider);
    List<Orden> findByLocal(Local local);
}
