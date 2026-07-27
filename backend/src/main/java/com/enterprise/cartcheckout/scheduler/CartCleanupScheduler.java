package com.enterprise.cartcheckout.scheduler;

import com.enterprise.cartcheckout.entity.Cart;
import com.enterprise.cartcheckout.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartCleanupScheduler {

    private final CartRepository cartRepository;

    @Scheduled(cron = "0 0 1 * * *") // Run every day at 1 AM
    public void cleanupExpiredCarts() {
        log.info("Running cart cleanup scheduler...");
        LocalDateTime now = LocalDateTime.now();
        
        List<Cart> expiredCarts = cartRepository.findAll().stream()
                .filter(c -> !c.isDeleted())
                .filter(c -> c.getExpiresAt() != null && c.getExpiresAt().isBefore(now))
                .toList();

        for (Cart cart : expiredCarts) {
            cart.setDeleted(true);
            cartRepository.save(cart);
            log.info("Deleted expired cart for user: {}", cart.getUserId());
        }
    }
}
