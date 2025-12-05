package com.pizzamdp.messaging.consumer;

import com.pizzamdp.messaging.config.RabbitConfig;
import com.pizzamdp.messaging.dto.OrderCreateEvent;
import com.pizzamdp.services.OrdersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderProcessingConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderProcessingConsumer.class);

    @Autowired
    private OrdersService ordersService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveOrderCreationRequest(OrderCreateEvent event) {
        String username = event.orden().getCliente().getUser().getUsername();
        log.info("Received order creation request for client: {}", username);
        try {
            // La lógica de negocio real para persistir la orden.
            // El método createOrder de OrdersService ya es transaccional.
            ordersService.createOrder(event.orden());
            log.info("Order processed and saved successfully for client: {}", username);
        } catch (Exception e) {
            log.error("Error processing order for client: {}. Error: {}", username, e.getMessage());
            // Lanza la excepción para que el mensaje sea reenviado o enviado a la DLQ
            // según la configuración del broker y del listener.
            throw e;
        }
    }
}
