package com.enterprise.cartcheckout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {
    private String id;
    private String userId;
    private List<CartItemResponse> items;
    private String couponCode;
    private boolean locked;
    private LocalDateTime expiresAt;
    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal shippingCharge;
    private BigDecimal grandTotal;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CartItemResponse {
        private String productId;
        private String sku;
        private String productName;
        private String image;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal subtotal;
        private boolean savedForLater;
    }
}
