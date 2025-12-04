package com.pizzamdp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.services.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;


@WebMvcTest(TamanioPizzaController.class)
@TestPropertySource(properties = {"spring.jpa.hibernate.ddl-auto=none"})
public class TamanioPizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private FlywayMigrationStrategy flywayMigrationStrategy; // Keep this to disable flyway

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    public void getAllTamanioPizzas_shouldReturnListOfPizzas() throws Exception {
        TamanioPizza pizza1 = new TamanioPizza(1, "Chica", 4);
        TamanioPizza pizza2 = new TamanioPizza(2, "Grande", 8);

        when(catalogService.getAllTamanios()).thenReturn(Arrays.asList(pizza1, pizza2));

        mockMvc.perform(get("/api/tamanio-pizzas").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nombre").value("Chica"))
                .andExpect(jsonPath("$[1].nombre").value("Grande"));
    }

    @Test
    @WithMockUser
    public void getTamanioPizzaById_shouldReturnPizza() throws Exception {
        TamanioPizza pizza = new TamanioPizza(1, "Chica", 4);

        when(catalogService.getTamanioById(1)).thenReturn(Optional.of(pizza));

        mockMvc.perform(get("/api/tamanio-pizzas/1").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica"));
    }

    @Test
    @WithMockUser
    public void createTamanioPizza_shouldCreatePizza() throws Exception {
        TamanioPizza pizza = new TamanioPizza(1, "Chica", 4);

        when(catalogService.createTamanio(any(TamanioPizza.class))).thenReturn(pizza);

        mockMvc.perform(post("/api/tamanio-pizzas").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica"));
    }

    @Test
    @WithMockUser
    public void updateTamanioPizza_shouldUpdatePizza() throws Exception {
        TamanioPizza updatedPizza = new TamanioPizza(1, "Chica Nueva", 5);

        when(catalogService.updateTamanio(any(Integer.class), any(TamanioPizza.class))).thenReturn(Optional.of(updatedPizza));

        mockMvc.perform(put("/api/tamanio-pizzas/1").with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPizza)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Chica Nueva"));
    }

    @Test
    @WithMockUser
    public void deleteTamanioPizza_shouldDeletePizza() throws Exception {
        when(catalogService.deleteTamanio(1)).thenReturn(true);

        mockMvc.perform(delete("/api/tamanio-pizzas/1").with(jwt()))
                .andExpect(status().isOk());
    }
}
