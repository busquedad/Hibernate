package com.pizzamdp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "staff")
@Data
@EqualsAndHashCode(callSuper = true)
public class Staff extends Persona {

    @ManyToOne
    @JoinColumn(name = "local_id", nullable = false)
    private Local local;

    @Enumerated(EnumType.STRING)
    private RolStaff rolOperativo;
}
