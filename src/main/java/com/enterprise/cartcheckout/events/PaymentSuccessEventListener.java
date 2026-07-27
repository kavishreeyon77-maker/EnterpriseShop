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
public class PaymentSuccessEventListener {

    private final RabbitTemplate rabbitTemplate;

    @Async
    @EventListener
    public void handlePaymentSuccessEvent(PaymentSuccessEvent event) {
        log.info("Handling PaymentSuccessEvent for payment: {}", event.getPaymentId());
        try {
            rabbitTemplate.convertAndSend(
                    AppConstants.EXCHANGE_PAYMENTS,
                    AppConstants.ROUTING_KEY_PAYMENT_SUCCESS,
                    event
            );
            log.info("Successfully published PaymentSuccessEvent to RabbitMQ");
        } catch (Exception e) {
            log.error("Failed to publish PaymentSuccessEvent to RabbitMQ", e);
        }
    }
}
