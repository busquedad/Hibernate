package com.pizzamdp.controllers;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tamanio-pizzas")
public class TamanioPizzaController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public List<TamanioPizza> getAllTamanioPizzas() {
        return catalogService.getAllTamanios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TamanioPizza> getTamanioPizzaById(@PathVariable Integer id) {
        return catalogService.getTamanioById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TamanioPizza createTamanioPizza(@RequestBody TamanioPizza tamanioPizza) {
        return catalogService.createTamanio(tamanioPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TamanioPizza> updateTamanioPizza(@PathVariable Integer id, @RequestBody TamanioPizza tamanioPizzaDetails) {
        return catalogService.updateTamanio(id, tamanioPizzaDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTamanioPizza(@PathVariable Integer id) {
        if (catalogService.deleteTamanio(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
