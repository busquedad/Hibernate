/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 * Represents the size of a pizza.
 * This class is mapped to the "TamanioPizza" table in the database.
 *
 * @author usuario
 */
public class TamanioPizza {
    private Integer id_tamanio_pizza;
    private String nombre;
    private int cant_porciones;

    /**
     * Default constructor.
     */
    public TamanioPizza() {
    }

    /**
     * Gets the ID of the pizza size.
     * @return The ID of the pizza size.
     */
    public Integer getId_tamanio_pizza() {
        return id_tamanio_pizza;
    }

    /**
     * Sets the ID of the pizza size.
     * @param id_tamanio_pizza The new ID for the pizza size.
     */
    public void setId_tamanio_pizza(Integer id_tamanio_pizza) {
        this.id_tamanio_pizza = id_tamanio_pizza;
    }

    /**
     * Gets the name of the pizza size.
     * @return The name of the pizza size.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Sets the name of the pizza size.
     * @param nombre The new name for the pizza size.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Gets the number of slices for the pizza size.
     * @return The number of slices.
     */
    public int getCant_porciones() {
        return cant_porciones;
    }

    /**
     * Sets the number of slices for the pizza size.
     * @param cant_porciones The new number of slices.
     */
    public void setCant_porciones(int cant_porciones) {
        this.cant_porciones = cant_porciones;
    }
    
}
