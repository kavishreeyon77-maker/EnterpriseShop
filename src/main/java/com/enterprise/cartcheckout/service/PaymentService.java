package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Payment;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.enums.PaymentStatus;

public interface PaymentService {
    Payment initiatePayment(String orderId, PaymentMethod paymentMethod);
    Payment processPaymentCallback(String transactionId, PaymentStatus status);
    Payment getPaymentByOrderId(String orderId);
    Payment refundPayment(String paymentId);
}
