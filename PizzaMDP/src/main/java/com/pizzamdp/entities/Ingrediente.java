package com.pizzamdp.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "ingredientes")
@Data
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String unidadMedida; // e.g., "gramos", "unidades"

    @Column(nullable = false)
    private Integer stockActual;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;
}
