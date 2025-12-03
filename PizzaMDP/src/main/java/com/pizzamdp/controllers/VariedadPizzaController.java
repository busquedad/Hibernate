package com.pizzamdp.controllers;

import com.pizzamdp.entities.VariedadPizza;
import com.pizzamdp.repositories.VariedadPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/variedad-pizzas")
public class VariedadPizzaController {

    @Autowired
    private VariedadPizzaRepository variedadPizzaRepository;

    @GetMapping
    public List<VariedadPizza> getAllVariedadPizzas() {
        return variedadPizzaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariedadPizza> getVariedadPizzaById(@PathVariable Integer id) {
        Optional<VariedadPizza> variedadPizza = variedadPizzaRepository.findById(id);
        return variedadPizza.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public VariedadPizza createVariedadPizza(@RequestBody VariedadPizza variedadPizza) {
        return variedadPizzaRepository.save(variedadPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariedadPizza> updateVariedadPizza(@PathVariable Integer id, @RequestBody VariedadPizza variedadPizzaDetails) {
        Optional<VariedadPizza> variedadPizzaOptional = variedadPizzaRepository.findById(id);
        if (variedadPizzaOptional.isPresent()) {
            VariedadPizza variedadPizza = variedadPizzaOptional.get();
            variedadPizza.setNombre(variedadPizzaDetails.getNombre());
            variedadPizza.setIngredientes(variedadPizzaDetails.getIngredientes());
            return ResponseEntity.ok(variedadPizzaRepository.save(variedadPizza));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariedadPizza(@PathVariable Integer id) {
        if (variedadPizzaRepository.existsById(id)) {
            variedadPizzaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
