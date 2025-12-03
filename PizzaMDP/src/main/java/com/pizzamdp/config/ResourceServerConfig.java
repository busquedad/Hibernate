package com.pizzamdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


/**
 * Configuración del Servidor de Recursos (Resource Server).
 * Protege los endpoints de la API y configura la validación de tokens JWT.
 */
@Configuration
public class ResourceServerConfig {

    /**
     * Define el filtro de seguridad para el Servidor de Recursos.
     * Configura las reglas de autorización basadas en roles para los diferentes endpoints de la API.
     * También registra el {@link JwtAuthenticationConverter} para interpretar los roles del token.
     *
     * @param http El constructor de seguridad HTTP.
     * @return El {@link SecurityFilterChain} configurado.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors().and()
            .csrf().disable()
            .authorizeHttpRequests(authorize -> authorize
                .antMatchers("/oms/ordenes", "/oms/ordenes/status").hasAnyRole("USUARIO", "ADMINISTRADOR")
                .antMatchers("/oms/**", "/catalogo/**", "/stock/**").hasRole("ADMINISTRADOR")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
            ));
        return http.build();
    }

    /**
     * Crea un convertidor para extraer las autoridades (roles) del token JWT.
     * Configura el convertidor para que lea el claim "roles" y añada el prefijo "ROLE_"
     * a cada rol, permitiendo la integración con las expresiones de seguridad de Spring.
     *
     * @return Un {@link JwtAuthenticationConverter} configurado.
     */
    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }
}
