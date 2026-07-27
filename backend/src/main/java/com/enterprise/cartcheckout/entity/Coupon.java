package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import com.enterprise.cartcheckout.enums.CouponType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Document(collection = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private CouponType discountType;

    private BigDecimal discountValue; 

    private BigDecimal minimumAmount; 

    private BigDecimal maximumDiscount; 

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;
    
    private Integer usageLimit;

    private Integer userLimit; 

    private Integer globalLimit; 

    @Builder.Default
    private Integer usedCount = 0; 

    @Builder.Default
    private Map<String, Integer> userUsageMap = new HashMap<>(); 

    @Builder.Default
    private boolean stackable = false;

    private String buyProductId;
    private Integer buyQuantity;
    private String getFreeProductId;
    private Integer getFreeQuantity;

    @Builder.Default
    private boolean active = true;
}
