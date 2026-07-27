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
public class WishlistResponse {
    private String id;
    private String userId;
    private List<WishlistItemResponse> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishlistItemResponse {
        private String productId;
        private String variantId;
        private String sku;
        private String productName;
        private String image;
        private BigDecimal price;
        private LocalDateTime addedAt;
    }
}
