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
public class TamanioPizza {
    public Integer id_tamanio_pizza;
    public String nombre;
    public int cant_porciones;

    public TamanioPizza() {
    }

    public Integer getId_tamanio_pizza() {
        return id_tamanio_pizza;
    }

    public void setId_tamanio_pizza(Integer id_tamanio_pizza) {
        this.id_tamanio_pizza = id_tamanio_pizza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCant_porciones() {
        return cant_porciones;
    }

    public void setCant_porciones(int cant_porciones) {
        this.cant_porciones = cant_porciones;
    }
    
}
