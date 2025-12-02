package entidades;

import org.junit.Test;
import static org.junit.Assert.*;

public class TamanioPizzaTest {

    @Test
    public void testGettersAndSetters() {
        TamanioPizza pizza = new TamanioPizza();

        // Test ID
        Integer id = 1;
        pizza.setId_tamanio_pizza(id);
        assertEquals(id, pizza.getId_tamanio_pizza());

        // Test Nombre
        String nombre = "Grande";
        pizza.setNombre(nombre);
        assertEquals(nombre, pizza.getNombre());

        // Test Cantidad de Porciones
        int porciones = 8;
        pizza.setCant_porciones(porciones);
        assertEquals(porciones, pizza.getCant_porciones());
    }
}
