package com.pizzamdp.messaging.dto;

import com.pizzamdp.entities.Orden;
import java.io.Serializable;

/**
 * Evento que se publica cuando se solicita la creación de una nueva orden.
 * Contiene la entidad Orden completa tal como se recibió en la solicitud.
 *
 * @param orden La entidad Orden a ser procesada.
 */
public record OrderCreateEvent(Orden orden) implements Serializable {
}
