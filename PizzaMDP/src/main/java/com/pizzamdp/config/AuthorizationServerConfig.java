package com.pizzamdp.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Configuración avanzada para el Servidor de Autorización OAuth2.
 * <p>
 * Esta clase establece la infraestructura central para la emisión y gestión de tokens OAuth2,
 * incluyendo el registro de clientes, la configuración de firmas criptográficas y la
 * personalización de los claims del JWT para incluir información de roles (RBAC).
 *
 * @see org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
 * @see <a href="https://tools.ietf.org/html/rfc6749" target="_blank">The OAuth 2.0 Authorization Framework (RFC 6749)</a>
 */
@Configuration
public class AuthorizationServerConfig {

    @Value("${security.oauth2.client.id}")
    private String clientId;

    /**
     * Define y configura el filtro de seguridad principal para el Servidor de Autorización.
     * <p>
     * Este filtro, con prioridad {@code @Order(1)}, aplica la configuración de seguridad por
     * defecto de Spring Authorization Server, que incluye los endpoints estándar de OAuth2
     * (e.g., {@code /oauth2/authorize}, {@code /oauth2/token}). También habilita la
     * autenticación de usuarios a través de un formulario de login estándar.
     *
     * @param http El constructor de seguridad HTTP para configurar el {@link SecurityFilterChain}.
     * @return Una instancia de {@link SecurityFilterChain} configurada para el servidor de autorización.
     * @throws Exception Si ocurre un error durante la configuración de la seguridad.
     * @see OAuth2AuthorizationServerConfiguration#applyDefaultSecurity(HttpSecurity)
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http.formLogin(Customizer.withDefaults()).build();
    }

    /**
     * Configura el repositorio de clientes OAuth2 registrados que pueden interactuar con este servidor.
     * <p>
     * Para esta implementación, se utiliza un {@link InMemoryRegisteredClientRepository}, que es adecuado
     * para entornos de demostración o con un número de clientes estático y limitado. El cliente
     * está configurado para el flujo de "Authorization Code" con PKCE, un estándar de seguridad
     * recomendado para aplicaciones de página única (SPA) y clientes públicos.
     *
     * @return Un {@link RegisteredClientRepository} que contiene la configuración del cliente.
     * @see RegisteredClient
     * @see <a href="https://tools.ietf.org/html/rfc7636" target="_blank">Proof Key for Code Exchange (PKCE)</a>
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId(clientId)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:5173/callback")
                .scope(OidcScopes.OPENID)
                .scope("pizza.read")
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(true) // Requiere consentimiento del usuario
                        .requireProofKey(true) // Habilita PKCE
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }

    /**
     * Crea y expone una {@link JWKSource} para la firma de tokens JWT.
     * <p>
     * Las claves JSON Web Key (JWK) son un estándar para representar claves criptográficas.
     * Esta implementación genera un par de claves RSA de 2048 bits en el arranque de la aplicación
     * y las expone a través de un {@link JWKSet}. El servidor de autorización utilizará estas
     * claves para firmar los JWT, y el servidor de recursos las usará para verificar la firma.
     *
     * @return Una fuente inmutable de claves JWK ({@link JWKSource}).
     * @see <a href="https://tools.ietf.org/html/rfc7517" target="_blank">JSON Web Key (JWK)</a>
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Genera un par de claves criptográficas RSA de 2048 bits de forma programática.
     *
     * @return El {@link KeyPair} (clave pública y privada) generado.
     * @throws IllegalStateException Si el algoritmo RSA no está disponible.
     */
    private static KeyPair generateRsaKey() {
        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
        return keyPair;
    }

    /**
     * Configura las opciones globales del Servidor de Autorización, como la URL del emisor (issuer).
     * <p>
     * El emisor es una URL única que identifica al servidor de autorización, un claim estándar
     * ({@code iss}) en los tokens JWT, fundamental para la validación de tokens en el lado del
     * servidor de recursos.
     *
     * @return Un objeto {@link AuthorizationServerSettings} con la configuración del emisor.
     * @see <a href="https://tools.ietf.org/html/rfc7519#section-4.1.1" target="_blank">JWT 'iss' (Issuer) Claim</a>
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().issuer("http://localhost:8080").build();
    }

    /**
     * Personaliza el contenido del token JWT para inyectar claims adicionales.
     * <p>
     * Este {@link OAuth2TokenCustomizer} enriquece el token JWT con un claim personalizado "roles".
     * Extrae las autoridades (roles) del principal de seguridad autenticado ({@code context.getPrincipal()}),
     * elimina el prefijo "ROLE_" para mayor limpieza, y las añade como una lista de strings.
     * Esto es crucial para implementar el Control de Acceso Basado en Roles (RBAC) en el Servidor de Recursos.
     *
     * @return Un {@link OAuth2TokenCustomizer} que añade el claim "roles" al JWT.
     */
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            Set<String> authorities = context.getPrincipal().getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replace("ROLE_", ""))
                    .collect(Collectors.toSet());
            context.getClaims().claim("roles", authorities);
        };
    }
}
