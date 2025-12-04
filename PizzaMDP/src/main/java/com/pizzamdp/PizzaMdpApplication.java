package com.pizzamdp;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PizzaMdpApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaMdpApplication.class, args);
    }

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
