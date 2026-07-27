package com.enterprise.cartcheckout.events;

import com.enterprise.cartcheckout.constants.AppConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderPlacedEventListener {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @EventListener
    public void handleOrderPlacedEvent(OrderPlacedEvent event) {
        log.info("Handling OrderPlacedEvent for order: {}", event.getOrderNumber());
        try {
            rabbitTemplate.convertAndSend(
                    AppConstants.EXCHANGE_ORDERS,
                    AppConstants.ROUTING_KEY_ORDER_PLACED,
                    event
            );
            log.info("Successfully published OrderPlacedEvent to RabbitMQ");
        } catch (Exception e) {
            log.error("Failed to publish OrderPlacedEvent to RabbitMQ", e);
        }
    }
}
