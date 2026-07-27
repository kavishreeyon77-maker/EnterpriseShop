package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import com.enterprise.cartcheckout.enums.OrderStatus;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.enums.PaymentStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String orderNumber;

    @Indexed
    private String userId;

    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    private Address shippingAddress; 

    private Address billingAddress; 
    
    private String addressId;

    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal shippingCharge;
    private BigDecimal grandTotal;

    private String couponCode;

    private PaymentMethod paymentMethod;
    
    private String paymentId;

    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Builder.Default
    private OrderStatus orderStatus = OrderStatus.PENDING;

    @Builder.Default
    private List<OrderStatusHistory> orderTimeline = new ArrayList<>();

    private String trackingNumber;

    private String invoiceNumber;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderItem {
        private String productId;
        private String variantId;
        private String sku;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal taxRate;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OrderStatusHistory {
        private OrderStatus status;
        private String description;
        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();
    }
}
