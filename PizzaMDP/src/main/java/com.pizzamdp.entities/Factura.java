package com.pizzamdp.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orden_id", nullable = false)
    private Orden orden;

    private String cae; // Codigo de Autorizacion Electronico
    private LocalDate fechaVencimientoCae;
    private LocalDate fechaFactura;
    private BigDecimal total;
    private String tipoFactura; // A, B, C
    private String prefijo;
    private String numeroFactura;
    private String urlPdf;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;
}
