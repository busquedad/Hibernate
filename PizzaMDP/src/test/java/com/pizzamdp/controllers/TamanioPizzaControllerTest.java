package com.pizzamdp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;


import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TamanioPizzaController.class)
public class TamanioPizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TamanioPizzaRepository tamanioPizzaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void getAllTamanioPizzas_shouldReturnListOfPizzas() throws Exception {
        TamanioPizza pizza1 = new TamanioPizza();
        pizza1.setId_tamanio_pizza(1);
        pizza1.setNombre("Chica");
        TamanioPizza pizza2 = new TamanioPizza();
        pizza2.setId_tamanio_pizza(2);
        pizza2.setNombre("Grande");

        when(tamanioPizzaRepository.findAll()).thenReturn(Arrays.asList(pizza1, pizza2));

        mockMvc.perform(get("/api/tamanio-pizzas").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Chica"))
                .andExpect(jsonPath("$[1].nombre").value("Grande"));
    }

    @Test
    @WithMockUser
    public void getTamanioPizzaById_shouldReturnPizza() throws Exception {
        TamanioPizza pizza = new TamanioPizza();
        pizza.setId_tamanio_pizza(1);
        pizza.setNombre("Chica");

        when(tamanioPizzaRepository.findById(1)).thenReturn(Optional.of(pizza));

        mockMvc.perform(get("/api/tamanio-pizzas/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica"));
    }

    @Test
    @WithMockUser
    public void createTamanioPizza_shouldCreatePizza() throws Exception {
        TamanioPizza pizza = new TamanioPizza();
        pizza.setId_tamanio_pizza(1);
        pizza.setNombre("Chica");

        when(tamanioPizzaRepository.save(any(TamanioPizza.class))).thenReturn(pizza);

        mockMvc.perform(post("/api/tamanio-pizzas").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica"));
    }

    @Test
    @WithMockUser
    public void updateTamanioPizza_shouldUpdatePizza() throws Exception {
        TamanioPizza pizza = new TamanioPizza();
        pizza.setId_tamanio_pizza(1);
        pizza.setNombre("Chica");

        TamanioPizza updatedPizza = new TamanioPizza();
        updatedPizza.setId_tamanio_pizza(1);
        updatedPizza.setNombre("Chica Nueva");

        when(tamanioPizzaRepository.findById(1)).thenReturn(Optional.of(pizza));
        when(tamanioPizzaRepository.save(any(TamanioPizza.class))).thenReturn(updatedPizza);

        mockMvc.perform(put("/api/tamanio-pizzas/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica Nueva"));
    }

    @Test
    @WithMockUser
    public void deleteTamanioPizza_shouldDeletePizza() throws Exception {
        when(tamanioPizzaRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(delete("/api/tamanio-pizzas/1").with(jwt()))
                .andExpect(status().isOk());
    }
}
