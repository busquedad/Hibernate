package com.pizzamdp.config;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class DataInitializerConfig {

    @Bean
    public CommandLineRunner bootstrapData(TamanioPizzaRepository tamanioPizzaRepository) {
        return (args) -> {
            if (tamanioPizzaRepository.count() == 0) {
                TamanioPizza t1 = new TamanioPizza(null, "Chica", 4);
                tamanioPizzaRepository.save(t1);

                TamanioPizza t2 = new TamanioPizza(null, "Grande", 8);
                tamanioPizzaRepository.save(t2);
            }
        };
    }
}
