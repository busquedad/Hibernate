package com.pizzamdp.entities;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Entity
@Table(name = "clientes")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona {

    @ElementCollection
    @CollectionTable(name = "cliente_direcciones", joinColumns = @JoinColumn(name = "cliente_id"))
    @Column(name = "direccion")
    private List<String> direcciones;

    private String preferencias;
}
