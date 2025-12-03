package com.pizzamdp.controllers;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tamanio-pizzas")
public class TamanioPizzaController {

    @Autowired
    private TamanioPizzaRepository tamanioPizzaRepository;

    @GetMapping
    public List<TamanioPizza> getAllTamanioPizzas() {
        return tamanioPizzaRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TamanioPizza> getTamanioPizzaById(@PathVariable Integer id) {
        Optional<TamanioPizza> tamanioPizza = tamanioPizzaRepository.findById(id);
        return tamanioPizza.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TamanioPizza createTamanioPizza(@RequestBody TamanioPizza tamanioPizza) {
        return tamanioPizzaRepository.save(tamanioPizza);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TamanioPizza> updateTamanioPizza(@PathVariable Integer id, @RequestBody TamanioPizza tamanioPizzaDetails) {
        Optional<TamanioPizza> tamanioPizzaOptional = tamanioPizzaRepository.findById(id);
        if (tamanioPizzaOptional.isPresent()) {
            TamanioPizza tamanioPizza = tamanioPizzaOptional.get();
            tamanioPizza.setNombre(tamanioPizzaDetails.getNombre());
            tamanioPizza.setCant_porciones(tamanioPizzaDetails.getCant_porciones());
            return ResponseEntity.ok(tamanioPizzaRepository.save(tamanioPizza));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTamanioPizza(@PathVariable Integer id) {
        if (tamanioPizzaRepository.existsById(id)) {
            tamanioPizzaRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
