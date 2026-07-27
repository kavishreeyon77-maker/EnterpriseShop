package com.enterprise.cartcheckout.dto.response;

import com.enterprise.cartcheckout.enums.OrderStatus;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.enums.PaymentStatus;
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
public class OrderResponse {
    private String id;
    private String orderNumber;
    private String userId;
    private List<OrderItemResponse> items;
    private AddressResponse shippingAddress;
    private AddressResponse billingAddress;
    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal shippingCharge;
    private BigDecimal grandTotal;
    private String couponCode;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private OrderStatus orderStatus;
    private String trackingNumber;
    private String invoiceNumber;
    private LocalDateTime createdAt;
    private List<OrderStatusHistoryResponse> orderTimeline;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse {
        private String productId;
        private String sku;
        private String productName;
        private BigDecimal price;
        private Integer quantity;
        private BigDecimal taxRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderStatusHistoryResponse {
        private OrderStatus status;
        private String description;
        private LocalDateTime timestamp;
    }
}
