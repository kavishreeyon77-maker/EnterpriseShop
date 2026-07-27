package com.enterprise.cartcheckout.scheduler;

import com.enterprise.cartcheckout.entity.Coupon;
import com.enterprise.cartcheckout.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponExpiryScheduler {

    private final CouponRepository couponRepository;

    @Scheduled(cron = "0 0 0 * * *") // Run every day at midnight
    public void deactivateExpiredCoupons() {
        log.info("Running coupon expiry scheduler...");
        LocalDateTime now = LocalDateTime.now();
        
        List<Coupon> expiredCoupons = couponRepository.findAll().stream()
                .filter(Coupon::isActive)
                .filter(c -> c.getExpiryDate() != null && c.getExpiryDate().isBefore(now))
                .toList();

        for (Coupon coupon : expiredCoupons) {
            coupon.setActive(false);
            couponRepository.save(coupon);
            log.info("Deactivated expired coupon: {}", coupon.getCode());
        }
    }
}
