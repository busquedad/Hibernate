package com.pizzamdp.listeners;

import com.pizzamdp.entities.Orden;
import com.pizzamdp.entities.User;
import com.pizzamdp.config.BeanUtil;
import com.pizzamdp.entities.Orden;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrdenEntityListener {

    @PostPersist
    @PostUpdate
    public void onOrdenChange(Orden orden) {
        SimpMessagingTemplate messagingTemplate = BeanUtil.getBean(SimpMessagingTemplate.class);

        // Notificar a los administradores
        messagingTemplate.convertAndSend("/topic/admin/orders", orden);

        // Notificar al cliente específico
        if (orden.getCliente() != null && orden.getCliente().getUsername() != null) {
            messagingTemplate.convertAndSendToUser(
                orden.getCliente().getUsername(),
                "/orders",
                orden
            );
        }
    }
}
