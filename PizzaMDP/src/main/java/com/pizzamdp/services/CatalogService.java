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

    @Transactional(readOnly = true)
    public Optional<TipoPizza> getTipoById(Integer id) { return tipoPizzaRepository.findById(id); }

    @Transactional
    public TipoPizza createTipo(TipoPizza tipo) { return tipoPizzaRepository.save(tipo); }

    @Transactional
    public Optional<TipoPizza> updateTipo(Integer id, TipoPizza details) {
        return tipoPizzaRepository.findById(id)
                .map(existing -> {
                    TipoPizza updated = new TipoPizza(existing.id_tipo_pizza(), details.nombre(), details.descripcionPizza());
                    return tipoPizzaRepository.save(updated);
                });
    }

    @Transactional
    public boolean deleteTipo(Integer id) {
        if (tipoPizzaRepository.existsById(id)) {
            tipoPizzaRepository.deleteById(id);
            return true;
        }
        return false;
    }


    // VariedadPizza methods
    @Transactional(readOnly = true)
    public List<VariedadPizza> getAllVariedades() { return variedadPizzaRepository.findAll(); }

    @Transactional(readOnly = true)
    public Optional<VariedadPizza> getVariedadById(Integer id) { return variedadPizzaRepository.findById(id); }

    @Transactional
    public VariedadPizza createVariedad(VariedadPizza variedad) { return variedadPizzaRepository.save(variedad); }

    @Transactional
    public Optional<VariedadPizza> updateVariedad(Integer id, VariedadPizza details) {
        return variedadPizzaRepository.findById(id)
                .map(existing -> {
                    VariedadPizza updated = new VariedadPizza(existing.id_variedad_pizza(), details.nombre(), details.ingredientes());
                    return variedadPizzaRepository.save(updated);
                });
    }

    @Transactional
    public boolean deleteVariedad(Integer id) {
        if (variedadPizzaRepository.existsById(id)) {
            variedadPizzaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
