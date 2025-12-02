/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

/**
 *
 * @author usuario
 */
public class VariedadPizza {
    public Integer id_variedad_pizza;
    public String nombre;
    public String ingredientes;

    public Integer getId_variedad_pizza() {
        return id_variedad_pizza;
    }

    public void setId_variedad_pizza(Integer id_variedad_pizza) {
        this.id_variedad_pizza = id_variedad_pizza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(String ingredientes) {
        this.ingredientes = ingredientes;
    }
    
}
