package com.pizzamdp.services;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class CatalogServiceTest {

    @Mock
    private TamanioPizzaRepository tamanioPizzaRepository;

    @InjectMocks
    private CatalogService catalogService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTamanios() {
        TamanioPizza tamanio = new TamanioPizza(1, "Grande", 8);
        when(tamanioPizzaRepository.findAll()).thenReturn(Collections.singletonList(tamanio));

        List<TamanioPizza> result = catalogService.getAllTamanios();

        assertEquals(1, result.size());
        assertEquals("Grande", result.get(0).nombre());
    }
}
