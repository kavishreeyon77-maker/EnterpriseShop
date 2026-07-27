package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Coupon;

import java.math.BigDecimal;

public interface CouponService {
    Coupon getCouponByCode(String code);
    boolean validateCoupon(String code, BigDecimal cartTotal);
    void recordCouponUsage(String code, String userId);
    Coupon createCoupon(Coupon coupon);
}
