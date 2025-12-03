package com.pizzamdp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para el Servidor de Recursos (Resource Server).
 * <p>
 * Esta clase es responsable de proteger los endpoints de la API. Define las reglas de
 * autorización (qué roles pueden acceder a qué rutas) y configura cómo el servidor debe
 * validar e interpretar los tokens de acceso JWT presentados por los clientes.
 *
 * @see org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
 * @see <a href="https://tools.ietf.org/html/rfc8259" target="_blank">OAuth 2.0 for Native Apps</a>
 */
@Configuration
public class ResourceServerConfig {

    /**
     * Define y configura el filtro de seguridad para el Servidor de Recursos.
     * <p>
     * Este filtro, con prioridad {@code @Order(2)}, se aplica a todas las solicitudes que
     * no fueron manejadas por el filtro del Servidor de Autorización. Sus responsabilidades
     * principales son:
     * <ul>
     *     <li>Deshabilitar CSRF, ya que la autenticación es sin estado (basada en tokens).</li>
     *     <li>Definir reglas de autorización específicas para las rutas de la API, utilizando
     *         expresiones de seguridad basadas en roles (e.g., {@code hasRole('ADMINISTRADOR')}).</li>
     *     <li>Configurar el servidor para que valide tokens JWT como mecanismo de autenticación.</li>
     *     <li>Registrar un {@link JwtAuthenticationConverter} para mapear los claims del JWT a
     *         autoridades de Spring Security.</li>
     * </ul>
     *
     * @param http El constructor de seguridad HTTP para configurar el {@link SecurityFilterChain}.
     * @return Una instancia de {@link SecurityFilterChain} configurada para el servidor de recursos.
     * @throws Exception Si ocurre un error durante la configuración de la seguridad.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http) throws Exception {
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
     * Crea un convertidor personalizado para extraer las autoridades (roles) de un token JWT.
     * <p>
     * Spring Security necesita saber cómo mapear la información de un JWT a su modelo de
     * seguridad interno ({@code GrantedAuthority}). Este convertidor se configura para:
     * <ol>
     *     <li>Leer el claim personalizado "roles" del JWT, que fue inyectado por el
     *         {@code OAuth2TokenCustomizer} en el Servidor de Autorización.</li>
     *     <li>Añadir el prefijo "ROLE_" a cada rol extraído. Este prefijo es una convención
     *         de Spring Security para que las expresiones como {@code hasRole()} funcionen
     *         correctamente.</li>
     * </ol>
     *
     * @return Un {@link JwtAuthenticationConverter} configurado para la aplicación.
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
