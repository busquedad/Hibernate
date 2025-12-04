package com.pizzamdp.controllers;

import com.pizzamdp.entities.TipoPizza;
import com.pizzamdp.services.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipo-pizzas")
public class TipoPizzaController {

    @Autowired
    private CatalogService catalogService;

    @GetMapping
    public List<TipoPizza> getAllTipoPizzas() {
        return catalogService.getAllTipos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPizza> getTipoPizzaById(@PathVariable Integer id) {
        return catalogService.getTipoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoPizza createTipoPizza(@RequestBody TipoPizza tipoPizza) {
        return catalogService.createTipo(tipoPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPizza> updateTipoPizza(@PathVariable Integer id, @RequestBody TipoPizza tipoPizzaDetails) {
        return catalogService.updateTipo(id, tipoPizzaDetails)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoPizza(@PathVariable Integer id) {
        if (catalogService.deleteTipo(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
