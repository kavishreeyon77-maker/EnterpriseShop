package com.enterprise.cartcheckout.events;

import com.enterprise.cartcheckout.constants.AppConstants;
import com.enterprise.cartcheckout.entity.Notification;
import com.enterprise.cartcheckout.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final RabbitTemplate rabbitTemplate;
    private final NotificationRepository notificationRepository;

    @Async
    @EventListener
    public void handleNotificationEvent(NotificationEvent event) {
        log.info("Handling NotificationEvent for user: {}", event.getUserId());
        
        Notification notification = Notification.builder()
                .userId(event.getUserId())
                .title(event.getTitle())
                .message(event.getMessage())
                .type(event.getType().name())
                .read(false)
                .build();
                
        notificationRepository.save(notification);

        try {
            rabbitTemplate.convertAndSend(
                    AppConstants.EXCHANGE_NOTIFICATIONS,
                    AppConstants.ROUTING_KEY_NOTIFICATION_SEND,
                    event
            );
            log.info("Successfully published NotificationEvent to RabbitMQ");
        } catch (Exception e) {
            log.error("Failed to publish NotificationEvent to RabbitMQ", e);
        }
    }
}
