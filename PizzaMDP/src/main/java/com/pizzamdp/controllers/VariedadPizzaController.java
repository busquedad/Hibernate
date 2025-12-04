package com.pizzamdp.controllers;

import com.pizzamdp.entities.VariedadPizza;
import com.pizzamdp.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/variedad-pizzas")
public class VariedadPizzaController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public List<VariedadPizza> getAllVariedadPizzas() {
        return catalogService.getAllVariedades();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariedadPizza> getVariedadPizzaById(@PathVariable Integer id) {
        return catalogService.getVariedadById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public VariedadPizza createVariedadPizza(@RequestBody VariedadPizza variedadPizza) {
        return catalogService.createVariedad(variedadPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariedadPizza> updateVariedadPizza(@PathVariable Integer id, @RequestBody VariedadPizza variedadPizzaDetails) {
        return catalogService.updateVariedad(id, variedadPizzaDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariedadPizza(@PathVariable Integer id) {
        if (catalogService.deleteVariedad(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
