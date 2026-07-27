package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Coupon;
import com.enterprise.cartcheckout.enums.CouponType;
import com.enterprise.cartcheckout.repository.CouponRepository;
import com.enterprise.cartcheckout.service.impl.CouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponServiceImpl couponService;

    private Coupon validPercentageCoupon;

    @BeforeEach
    void setUp() {
        validPercentageCoupon = Coupon.builder()
                .id("coupon-001")
                .code("SAVE20")
                .discountType(CouponType.PERCENTAGE)
                .discountValue(new BigDecimal("20"))
                .minimumAmount(new BigDecimal("200"))
                .maximumDiscount(new BigDecimal("100"))
                .expiryDate(LocalDateTime.now().plusDays(30))
                .usageLimit(100)
                .userLimit(1)
                .usedCount(0)
                .userUsageMap(new HashMap<>())
                .stackable(false)
                .active(true)
                .build();
    }

    @Test
    void getCouponByCode_shouldReturnCoupon() {
        when(couponRepository.findByCodeAndDeletedFalse("SAVE20"))
                .thenReturn(Optional.of(validPercentageCoupon));

        Coupon coupon = couponService.getCouponByCode("SAVE20");

        assertThat(coupon).isNotNull();
        assertThat(coupon.getCode()).isEqualTo("SAVE20");
        assertThat(coupon.getDiscountType()).isEqualTo(CouponType.PERCENTAGE);
    }

    @Test
    void validateCoupon_shouldReturnTrue_whenValid() {
        when(couponRepository.findByCodeAndDeletedFalse("SAVE20"))
                .thenReturn(Optional.of(validPercentageCoupon));

        boolean valid = couponService.validateCoupon("SAVE20", new BigDecimal("500"));

        assertThat(valid).isTrue();
    }

    @Test
    void validateCoupon_shouldReturnFalse_whenExpired() {
        validPercentageCoupon.setExpiryDate(LocalDateTime.now().minusDays(1));
        when(couponRepository.findByCodeAndDeletedFalse("SAVE20"))
                .thenReturn(Optional.of(validPercentageCoupon));

        boolean valid = couponService.validateCoupon("SAVE20", new BigDecimal("500"));

        assertThat(valid).isFalse();
    }

    @Test
    void validateCoupon_shouldReturnFalse_whenBelowMinimumAmount() {
        when(couponRepository.findByCodeAndDeletedFalse("SAVE20"))
                .thenReturn(Optional.of(validPercentageCoupon));

        boolean valid = couponService.validateCoupon("SAVE20", new BigDecimal("100"));

        assertThat(valid).isFalse();
    }

    @Test
    void validateCoupon_shouldReturnFalse_whenUsageLimitReached() {
        validPercentageCoupon.setUsedCount(100);
        when(couponRepository.findByCodeAndDeletedFalse("SAVE20"))
                .thenReturn(Optional.of(validPercentageCoupon));

        boolean valid = couponService.validateCoupon("SAVE20", new BigDecimal("500"));

        assertThat(valid).isFalse();
    }

    @Test
    void getCouponByCode_shouldThrow_whenNotFound() {
        when(couponRepository.findByCodeAndDeletedFalse("INVALID"))
                .thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(RuntimeException.class,
                () -> couponService.getCouponByCode("INVALID"));
    }
}
