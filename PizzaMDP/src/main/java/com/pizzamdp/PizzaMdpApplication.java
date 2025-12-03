package com.pizzamdp;

import com.pizzamdp.entities.TamanioPizza;
import com.pizzamdp.repositories.TamanioPizzaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PizzaMdpApplication {

    public static void main(String[] args) {
        SpringApplication.run(PizzaMdpApplication.class, args);
    }

    @Bean
    public CommandLineRunner bootstrapData(TamanioPizzaRepository tamanioPizzaRepository) {
        return (args) -> {
            if (tamanioPizzaRepository.count() == 0) {
                TamanioPizza t1 = new TamanioPizza();
                t1.setNombre("Chica");
                t1.setCant_porciones(4);
                tamanioPizzaRepository.save(t1);

                TamanioPizza t2 = new TamanioPizza();
                t2.setNombre("Grande");
                t2.setCant_porciones(8);
                tamanioPizzaRepository.save(t2);
            }
        };
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                String allowedOrigin = System.getenv("ALLOWED_ORIGIN");
                if (allowedOrigin == null) {
                    allowedOrigin = "http://localhost:5173";
                }
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigin)
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
            }
        };
    }
}
