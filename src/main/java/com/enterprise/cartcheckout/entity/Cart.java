package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true, sparse = true)
    private String userId; 

    @Builder.Default
    private List<CartItem> items = new ArrayList<>();

    private String couponCode;

    @Builder.Default
    private boolean locked = false; 

    private LocalDateTime expiresAt; 
    
    private String status;

    @Builder.Default
    private BigDecimal subTotal = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal tax = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal shippingCharge = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal grandTotal = BigDecimal.ZERO;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CartItem {
        private String productId;
        private String variantId; 
        private String sku; 
        private String productName;
        private String image;
        private BigDecimal price; 
        private Integer quantity;
        private BigDecimal taxRate;
        private BigDecimal subtotal;
        
        @Builder.Default
        private boolean savedForLater = false; 
    }
}
