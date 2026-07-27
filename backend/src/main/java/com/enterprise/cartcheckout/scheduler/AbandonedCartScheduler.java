package com.enterprise.cartcheckout.scheduler;

import com.enterprise.cartcheckout.entity.Cart;
import com.enterprise.cartcheckout.enums.NotificationType;
import com.enterprise.cartcheckout.events.NotificationEvent;
import com.enterprise.cartcheckout.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AbandonedCartScheduler {

    private final CartRepository cartRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 * * * *") // Run every hour
    public void processAbandonedCarts() {
        log.info("Running abandoned cart scheduler...");
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        
        List<Cart> abandonedCarts = cartRepository.findAll().stream()
                .filter(c -> !c.isDeleted() && !c.getItems().isEmpty())
                .filter(c -> c.getUpdatedAt() != null && c.getUpdatedAt().isBefore(twentyFourHoursAgo))
                .toList();

        for (Cart cart : abandonedCarts) {
            eventPublisher.publishEvent(new NotificationEvent(
                    cart.getUserId(),
                    "You left items in your cart!",
                    "Come back and complete your checkout for the items you left behind.",
                    NotificationType.SYSTEM
            ));
            log.info("Sent abandoned cart notification to user: {}", cart.getUserId());
        }
    }
}
