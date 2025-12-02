/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 * Represents the variety of a pizza.
 * This class is mapped to the "VariedadPizza" table in the database.
 *
 * @author usuario
 */
public class VariedadPizza {
    private Integer id_variedad_pizza;
    private String nombre;
    private String ingredientes;

    /**
     * Default constructor.
     */
    public VariedadPizza() {
    }

    /**
     * Gets the ID of the pizza variety.
     * @return The ID of the pizza variety.
     */
    public Integer getId_variedad_pizza() {
        return id_variedad_pizza;
    }

    /**
     * Sets the ID of the pizza variety.
     * @param id_variedad_pizza The new ID for the pizza variety.
     */
    public void setId_variedad_pizza(Integer id_variedad_pizza) {
        this.id_variedad_pizza = id_variedad_pizza;
    }

    /**
     * Gets the name of the pizza variety.
     * @return The name of the pizza variety.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the name of the pizza variety.
     * @param nombre The new name for the pizza variety.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the ingredients of the pizza variety.
     * @return The ingredients of the pizza variety.
     */
    public String getIngredientes() {
        return ingredientes;
    }

    /**
     * Sets the ingredients of the pizza variety.
     * @param ingredientes The new ingredients for the pizza variety.
     */
    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
    
}
