package com.pizzamdp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipopizza")
public record TipoPizza(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_pizza")
    Integer id_tipo_pizza,

    @Column(name = "nombre")
    String nombre,

    @Column(name = "descripcion")
    String descripcionPizza
) {}
