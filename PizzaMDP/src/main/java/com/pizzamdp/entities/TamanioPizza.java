package com.pizzamdp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tamaniopizza")
public record TamanioPizza(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tamanio_pizza")
    Integer id_tamanio_pizza,

    @Column(name = "nombre")
    String nombre,

    @Column(name = "cant_porciones")
    int cant_porciones
) {}
