
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
 * Configuración de seguridad para la autenticación de usuarios.
 * <p>
 * Esta clase define cómo la aplicación gestiona los detalles de los usuarios para la
 * autenticación. Para fines de demostración, utiliza un {@link UserDetailsService}
 * que almacena los usuarios en memoria. En un entorno de producción, esto debería ser
 * reemplazado por una implementación que se conecte a una base de datos de usuarios
 * (e.g., a través de JPA) u otro proveedor de identidades.
 */
@Configuration
public class DefaultSecurityConfig {

    /**
     * Crea un bean {@link UserDetailsService} con un conjunto de usuarios de prueba en memoria.
     * <p>
     * Este servicio es fundamental para que Spring Security pueda cargar los detalles de un
     * usuario (incluyendo contraseña y roles) durante el proceso de autenticación. Aquí se definen
     * dos usuarios:
     * <ul>
     *     <li><b>admin</b>: Con el rol {@code ADMINISTRADOR}.</li>
     *     <li><b>user</b>: Con el rol {@code USUARIO}.</li>
     * </ul>
     * Las contraseñas se codifican utilizando el {@link PasswordEncoder} delegado de Spring,
     * que soporta múltiples algoritmos de codificación y es la práctica recomendada.
     *
     * @return Un {@link InMemoryUserDetailsManager} pre-poblado con los usuarios de demostración.
     * @see UserDetailsService
     * @see PasswordEncoderFactories#createDelegatingPasswordEncoder()
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
