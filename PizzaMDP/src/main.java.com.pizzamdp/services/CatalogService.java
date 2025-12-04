package com.pizzamdp.services;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.entities.TipoPizza;
import com.pizzamdp.entities.VariedadPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import com.pizzamdp.repositories.TipoPizzaRepository;
import com.pizzamdp.repositories.VariedadPizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {

    @Autowired
    private TamanioPizzaRepository tamanioPizzaRepository;

    @Autowired
    private TipoPizzaRepository tipoPizzaRepository;

    @Autowired
    private VariedadPizzaRepository variedadPizzaRepository;

    // TamanioPizza methods
    @Transactional(readOnly = true)
    public List<TamanioPizza> getAllTamanios() { return tamanioPizzaRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<TamanioPizza> getTamanioById(Integer id) { return tamanioPizzaRepository.findById(id); }

    @Transactional
    public TamanioPizza createTamanio(TamanioPizza tamanio) { return tamanioPizzaRepository.save(tamanio); }

    @Transactional
    public Optional<TamanioPizza> updateTamanio(Integer id, TamanioPizza details) {
        return tamanioPizzaRepository.findById(id)
                .map(existing -> {
                    TamanioPizza updated = new TamanioPizza(existing.id_tamanio_pizza(), details.nombre(), details.cant_porciones());
                    return tamanioPizzaRepository.save(updated);
                });
    }

    @Transactional
    public boolean deleteTamanio(Integer id) {
        if (tamanioPizzaRepository.existsById(id)) {
            tamanioPizzaRepository.deleteById(id);
            return true;
        }
        return false;
    }


    // TipoPizza methods
    @Transactional(readOnly = true)
    public List<TipoPizza> getAllTipos() { return tipoPizzaRepository.findAll(); }

    // ... (Get, Create, Update, Delete for TipoPizza would be here)


    // VariedadPizza methods
    @Transactional(readOnly = true)
    public List<VariedadPizza> getAllVariedades() { return variedadPizzaRepository.findAll(); }

    // ... (Get, Create, Update, Delete for VariedadPizza would be here)

}
