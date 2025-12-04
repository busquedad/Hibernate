package com.pizzamdp.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orden")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer id;

    @Column(name = "nombre_cliente", nullable = false)
    private String nombreCliente;

    @Column(name = "direccion_cliente", nullable = false)
    private String direccionCliente;

    @Column(name = "fecha_orden", nullable = false)
    private LocalDateTime fechaOrden;

    @Column(name = "estado_orden", nullable = false)
    private String estadoOrden;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrdenItem> items;
}
