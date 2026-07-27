package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.AppConstants;
import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Coupon;
import com.enterprise.cartcheckout.repository.CouponRepository;
import com.enterprise.cartcheckout.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;

    @Override
    @Cacheable(value = AppConstants.CACHE_COUPONS, key = "#code")
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCodeAndDeletedFalse(code)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.COUPON_NOT_FOUND));
    }

    @Override
    public boolean validateCoupon(String code, BigDecimal cartTotal) {
        Coupon coupon = getCouponByCode(code);
        
        if (!coupon.isActive()) return false;
        
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) return false;
        if (coupon.getExpiryDate() != null && now.isAfter(coupon.getExpiryDate())) return false;
        
        if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) return false;
        
        if (coupon.getMinimumAmount() != null && cartTotal.compareTo(coupon.getMinimumAmount()) < 0) return false;

        return true;
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_COUPONS, key = "#code")
    public void recordCouponUsage(String code, String userId) {
        Coupon coupon = getCouponByCode(code);
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        
        if (userId != null && coupon.getUserLimit() != null) {
            int userUsage = coupon.getUserUsageMap().getOrDefault(userId, 0);
            coupon.getUserUsageMap().put(userId, userUsage + 1);
        }
        
        couponRepository.save(coupon);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_COUPONS, allEntries = true)
    public Coupon createCoupon(Coupon coupon) {
        return couponRepository.save(coupon);
    }
}
