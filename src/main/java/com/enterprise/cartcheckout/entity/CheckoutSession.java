package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import com.enterprise.cartcheckout.enums.CheckoutStatus;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "checkout_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckoutSession extends BaseEntity {

    @Id
    private String id;

    @Indexed
    private String userId;

    private String cartId;

    @Indexed(unique = true)
    private String checkoutToken;

    private String shippingAddressId;

    private String billingAddressId;

    private String couponCode;

    private String shippingMethod; // STANDARD, EXPRESS

    private PaymentMethod paymentMethod;

    @Builder.Default
    private List<ReservedItem> reservedItems = new ArrayList<>();

    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal grandTotal;

    private LocalDateTime expiresAt;

    @Builder.Default
    private CheckoutStatus status = CheckoutStatus.INITIATED;

    private String orderId;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReservedItem {
        private String sku;
        private Integer quantity;
    }
}
