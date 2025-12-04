package com.pizzamdp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "variedadpizza")
public record VariedadPizza(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variedad_pizza")
    Integer id_variedad_pizza,

    @Column(name = "nombre")
    String nombre,

    @Column(name = "ingredientes")
    String ingredientes
) {}
