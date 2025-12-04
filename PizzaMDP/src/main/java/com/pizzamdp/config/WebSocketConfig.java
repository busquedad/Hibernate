package com.pizzamdp.config;

import org.springframework.beans.factory.annotation.Value;
import com.pizzamdp.security.JwtAuthChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${cors.allowed-origin:http://localhost:5173}")
    private String allowedOrigin;

    @Autowired
    private JwtAuthChannelInterceptor jwtAuthChannelInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilita un broker de mensajes simple en memoria para los destinos que comienzan con /topic y /user.
        config.enableSimpleBroker("/topic", "/user");
        // Define el prefijo para los mensajes que se dirigen desde el cliente al servidor (e.g., @MessageMapping).
        config.setApplicationDestinationPrefixes("/app");
        // Define el prefijo para los destinos de usuario específicos, usado por SimpMessagingTemplate.convertAndSendToUser.
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Registra el endpoint /ws-pizza que los clientes usarán para conectarse al WebSocket.
        // se configura el origen permitido para CORS y se habilita SockJS como fallback.
        registry.addEndpoint("/ws-pizza")
                .setAllowedOrigins(allowedOrigin)
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(jwtAuthChannelInterceptor);
    }
}
