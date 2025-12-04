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
        return variedadPizzaRepository.findById(id)
                .map(existingVariedad -> {
                    VariedadPizza updatedVariedad = new VariedadPizza(
                            existingVariedad.id_variedad_pizza(),
                            variedadPizzaDetails.nombre(),
                            variedadPizzaDetails.ingredientes()
                    );
                    return ResponseEntity.ok(variedadPizzaRepository.save(updatedVariedad));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
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
