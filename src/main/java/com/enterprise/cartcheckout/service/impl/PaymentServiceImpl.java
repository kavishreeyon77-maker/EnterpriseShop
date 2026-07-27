package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.entity.Payment;
import com.enterprise.cartcheckout.enums.OrderStatus;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.enums.PaymentStatus;
import com.enterprise.cartcheckout.events.PaymentSuccessEvent;
import com.enterprise.cartcheckout.repository.PaymentRepository;
import com.enterprise.cartcheckout.service.OrderService;
import com.enterprise.cartcheckout.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Payment initiatePayment(String orderId, PaymentMethod paymentMethod) {
        Order order = orderService.getOrder(orderId);
        
        Payment payment = Payment.builder()
                .orderId(order.getId())
                .userId(order.getUserId())
                .amount(order.getGrandTotal())
                .paymentMethod(paymentMethod)
                .status(PaymentStatus.PENDING)
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase())
                .build();
                
        payment.getPaymentLogs().add(Payment.PaymentLog.builder()
                .message("Payment initiated")
                .statusTo(PaymentStatus.PENDING)
                .build());
                
        return paymentRepository.save(payment);
    }

    @Override
    public Payment processPaymentCallback(String transactionId, PaymentStatus status) {
        Payment payment = paymentRepository.findByTransactionIdAndDeletedFalse(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
                
        PaymentStatus oldStatus = payment.getStatus();
        payment.setStatus(status);
        
        if (status == PaymentStatus.SUCCESS) {
            payment.setPaidAt(LocalDateTime.now());
        }
        
        payment.getPaymentLogs().add(Payment.PaymentLog.builder()
                .message("Payment status updated via callback")
                .statusFrom(oldStatus)
                .statusTo(status)
                .build());
                
        Payment savedPayment = paymentRepository.save(payment);
        
        if (status == PaymentStatus.SUCCESS) {
            orderService.updateOrderStatus(savedPayment.getOrderId(), OrderStatus.CONFIRMED, "Payment successful");
            eventPublisher.publishEvent(new PaymentSuccessEvent(savedPayment.getId(), savedPayment.getOrderId(), savedPayment.getTransactionId()));
        } else if (status == PaymentStatus.FAILED) {
            orderService.updateOrderStatus(savedPayment.getOrderId(), OrderStatus.CANCELLED, "Payment failed");
        }
        
        return savedPayment;
    }

    @Override
    public Payment getPaymentByOrderId(String orderId) {
        return paymentRepository.findByOrderIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found for order"));
    }

    @Override
    public Payment refundPayment(String paymentId) {
        Payment payment = paymentRepository.findByIdAndDeletedFalse(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
                
        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new RuntimeException("Cannot refund incomplete payment");
        }
        
        PaymentStatus oldStatus = payment.getStatus();
        payment.setStatus(PaymentStatus.REFUNDED);
        
        payment.getPaymentLogs().add(Payment.PaymentLog.builder()
                .message("Payment refunded")
                .statusFrom(oldStatus)
                .statusTo(PaymentStatus.REFUNDED)
                .build());
                
        Payment savedPayment = paymentRepository.save(payment);
        orderService.updateOrderStatus(savedPayment.getOrderId(), OrderStatus.REFUNDED, "Payment refunded");
        
        return savedPayment;
    }
}
