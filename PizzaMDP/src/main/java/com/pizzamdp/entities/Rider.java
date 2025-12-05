package com.pizzamdp.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Entity
@Table(name = "riders")
@Data
@EqualsAndHashCode(callSuper = true)
public class Rider extends Persona {

    private String vehiculo;
    private String patente;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitudActual;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitudActual;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoRider estado;
}
