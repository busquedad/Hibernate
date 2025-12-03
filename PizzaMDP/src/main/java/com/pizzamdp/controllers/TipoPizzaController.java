package com.pizzamdp.controllers;

import com.pizzamdp.entities.TipoPizza;
import com.pizzamdp.repositories.TipoPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tipo-pizzas")
public class TipoPizzaController {

    @Autowired
    private TipoPizzaRepository tipoPizzaRepository;

    @GetMapping
    public List<TipoPizza> getAllTipoPizzas() {
        return tipoPizzaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoPizza> getTipoPizzaById(@PathVariable Integer id) {
        Optional<TipoPizza> tipoPizza = tipoPizzaRepository.findById(id);
        return tipoPizza.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TipoPizza createTipoPizza(@RequestBody TipoPizza tipoPizza) {
        return tipoPizzaRepository.save(tipoPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoPizza> updateTipoPizza(@PathVariable Integer id, @RequestBody TipoPizza tipoPizzaDetails) {
        Optional<TipoPizza> tipoPizzaOptional = tipoPizzaRepository.findById(id);
        if (tipoPizzaOptional.isPresent()) {
            TipoPizza tipoPizza = tipoPizzaOptional.get();
            tipoPizza.setNombre(tipoPizzaDetails.getNombre());
            tipoPizza.setDescripcionPizza(tipoPizzaDetails.getDescripcionPizza());
            return ResponseEntity.ok(tipoPizzaRepository.save(tipoPizza));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoPizza(@PathVariable Integer id) {
        if (tipoPizzaRepository.existsById(id)) {
            tipoPizzaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
