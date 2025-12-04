package com.pizzamdp.entities;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orden_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdenItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden_item")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_orden", nullable = false)
    private Orden orden;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_variedad_pizza", nullable = false)
    private VariedadPizza variedadPizza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tamanio_pizza", nullable = false)
    private TamanioPizza tamanioPizza;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_pizza", nullable = false)
    private TipoPizza tipoPizza;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;
}
