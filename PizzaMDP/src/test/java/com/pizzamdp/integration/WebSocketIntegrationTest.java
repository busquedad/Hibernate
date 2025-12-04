package com.pizzamdp.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import com.pizzamdp.repositories.OrdenRepository;
import com.pizzamdp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

import com.pizzamdp.config.AuthorizationServerConfig;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration"
)
@ActiveProfiles("test")
public class WebSocketIntegrationTest {

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @TestConfiguration
    static class TestConfig {
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

        private static KeyPair generateRsaKey() {
            try {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
                keyPairGenerator.initialize(2048);
                return keyPairGenerator.generateKeyPair();
            } catch (Exception ex) {
                throw new IllegalStateException(ex);
            }
        }

        @Bean
        public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
            return new NimbusJwtEncoder(jwkSource);
        }
    }

    @LocalServerPort
    private int port;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private WebSocketStompClient stompClient;
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    public void setup() {
        ordenRepository.deleteAll();
        userRepository.deleteAll();
        List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
        this.stompClient = new WebSocketStompClient(new SockJsClient(transports));
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setObjectMapper(objectMapper);
        this.stompClient.setMessageConverter(messageConverter);
    }

    @Test
    void whenOrderIsCreated_thenNotificationIsReceivedViaWebSocket() throws Exception {
        // 1. Crear usuario y generar token
        User testUser = userRepository.save(User.builder()
                .username("testuser-ws")
                .password(passwordEncoder.encode("password"))
                .roles(Collections.singletonList("CLIENTE"))
                .provider(com.pizzamdp.entities.Provider.LOCAL)
                .build());
        String token = generateTokenForUser(testUser);

        // 2. Configurar cliente WebSocket y futuros para los mensajes
        CompletableFuture<Orden> userFuture = new CompletableFuture<>();
        CompletableFuture<Orden> adminFuture = new CompletableFuture<>();

        StompHeaders connectHeaders = new StompHeaders();
        connectHeaders.add("Authorization", "Bearer " + token);

        StompSession stompSession = stompClient.connectAsync(String.format("ws://localhost:%d/ws-pizza", port), new org.springframework.web.socket.WebSocketHttpHeaders(), connectHeaders, new StompSessionHandlerAdapter() {})
                .get(5, TimeUnit.SECONDS);

        // 3. Suscribirse a los topics
        // El destino para un usuario específico se resuelve a /user/{username}/orders
        stompSession.subscribe("/user/orders", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Orden.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                userFuture.complete((Orden) payload);
            }
        });

        stompSession.subscribe("/topic/admin/orders", new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return Orden.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                adminFuture.complete((Orden) payload);
            }
        });

        // 4. Crear una nueva orden para disparar el evento
        Orden newOrder = new Orden(null, testUser, null, LocalDateTime.now(), "CREADA", null);
        ordenRepository.save(newOrder);

        // 5. Verificar que se recibieron los mensajes
        Orden receivedUserOrder = userFuture.get(10, TimeUnit.SECONDS);
        Orden receivedAdminOrder = adminFuture.get(10, TimeUnit.SECONDS);

        assertThat(receivedUserOrder).isNotNull();
        assertThat(receivedUserOrder.getId()).isEqualTo(newOrder.getId());
        assertThat(receivedUserOrder.getCliente().getUsername()).isEqualTo(testUser.getUsername());

        assertThat(receivedAdminOrder).isNotNull();
        assertThat(receivedAdminOrder.getId()).isEqualTo(newOrder.getId());
    }

    private String generateTokenForUser(User user) {
        Instant now = Instant.now();
        long expiry = 36000L;
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream().map(ga -> ga.getAuthority().replace("ROLE_", "")).toList())
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
