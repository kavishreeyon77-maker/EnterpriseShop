package com.enterprise.cartcheckout.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PaymentSuccessEvent {
    private String paymentId;
    private String orderId;
    private String transactionId;
}
