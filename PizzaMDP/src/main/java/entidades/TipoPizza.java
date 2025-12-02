/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 * Represents the type of a pizza.
 * This class is mapped to the "TipoPizza" table in the database.
 *
 * @author PC-MATT
 */
public class TipoPizza {
    private Integer id_tipo_pizza;
    private String nombre;
    private String descripcionPizza;

    /**
     * Default constructor.
     */
    public TipoPizza() {
    }

    /**
     * Gets the description of the pizza type.
     * @return The description of the pizza type.
     */
    public String getDescripcionPizza() {
        return descripcionPizza;
    }

    /**
     * Sets the description of the pizza type.
     * @param descripcionPizza The new description for the pizza type.
     */
    public void setDescripcionPizza(String descripcionPizza) {
        this.descripcionPizza = descripcionPizza;
    }
    
    /**
     * Gets the ID of the pizza type.
     * @return The ID of the pizza type.
     */
    public Integer getId_tipo_pizza() {
        return id_tipo_pizza;
    }

    /**
     * Sets the ID of the pizza type.
     * @param id_tipo_pizza The new ID for the pizza type.
     */
    public void setId_tipo_pizza(Integer id_tipo_pizza) {
        this.id_tipo_pizza = id_tipo_pizza;
    }

    /**
     * Gets the name of the pizza type.
     * @return The name of the pizza type.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the name of the pizza type.
     * @param nombre The new name for the pizza type.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
