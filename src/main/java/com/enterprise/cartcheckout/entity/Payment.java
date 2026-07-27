package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
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

@Document(collection = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    private String id;

    @Indexed
    private String orderId;

    private String userId;

    @Indexed(unique = true)
    private String transactionId;

    private BigDecimal amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus status;
    
    private LocalDateTime paidAt;

    @Builder.Default
    private List<PaymentLog> paymentLogs = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentLog {
        private String message;
        private PaymentStatus statusFrom;
        private PaymentStatus statusTo;
        @Builder.Default
        private LocalDateTime timestamp = LocalDateTime.now();
    }
}
