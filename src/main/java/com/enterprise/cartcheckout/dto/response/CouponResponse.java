package com.enterprise.cartcheckout.dto.response;

import com.enterprise.cartcheckout.enums.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponse {
    private String id;
    private String code;
    private CouponType discountType;
    private BigDecimal discountValue;
    private BigDecimal minimumAmount;
    private BigDecimal maximumDiscount;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private boolean active;
}
