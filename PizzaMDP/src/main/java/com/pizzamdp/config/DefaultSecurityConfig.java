package com.pizzamdp.config;

import com.pizzamdp.services.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * Configuración de seguridad principal que gestiona la autenticación de usuarios
 * a través de múltiples mecanismos (login con formulario y OAuth2/OIDC).
 */
@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {

    private final CustomOidcUserService customOidcUserService;

    public DefaultSecurityConfig(CustomOidcUserService customOidcUserService) {
        this.customOidcUserService = customOidcUserService;
    }

    /**
     * Define el {@link SecurityFilterChain} principal que protege los endpoints.
     * <p>
     * Configura lo siguiente:
     * <ul>
     *     <li>Todas las peticiones deben ser autenticadas.</li>
     *     <li>Habilita el login por formulario estándar ({@code formLogin}).</li>
     *     <li>Habilita el login con OAuth2/OIDC ({@code oauth2Login}) para federación de identidades
     *     (e.g., Google). Se conecta con un {@link CustomOidcUserService} para el provisionamiento
     *     automático de usuarios.</li>
     * </ul>
     *
     * @param http El objeto {@link HttpSecurity} para configurar.
     * @return La cadena de filtros de seguridad construida.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOidcUserService)
                )
            );
        return http.build();
    }

    /**
     * Proporciona un {@link PasswordEncoder} para la codificación y validación de contraseñas.
     * <p>
     * Utiliza el codificador delegado de Spring, que es la práctica recomendada por su
     * flexibilidad y seguridad (soporta bcrypt por defecto).
     *
     * @return Una instancia de {@link PasswordEncoder}.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
