package com.pizzamdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

/**
 * Configuración de seguridad por defecto para la autenticación de usuarios.
 * Define un {@link UserDetailsService} en memoria para propósitos de demostración.
 */
@Configuration
public class DefaultSecurityConfig {

    /**
     * Crea un bean {@link UserDetailsService} con usuarios de prueba en memoria.
     * Define un usuario "admin" con rol "ADMINISTRADOR" y un usuario "user" con rol "USUARIO".
     * Las contraseñas se codifican utilizando el {@link PasswordEncoder} delegado de Spring.
     *
     * @return Un {@link InMemoryUserDetailsManager} con los usuarios de prueba.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        UserDetails admin = User.builder()
                .username("admin")
                .password(encoder.encode("password"))
                .roles("ADMINISTRADOR")
                .build();
        UserDetails user = User.builder()
                .username("user")
                .password(encoder.encode("password"))
                .roles("USUARIO")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
