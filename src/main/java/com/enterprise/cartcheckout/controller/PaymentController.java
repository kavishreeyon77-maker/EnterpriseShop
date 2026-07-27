package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.PaymentResponse;
import com.enterprise.cartcheckout.entity.Payment;
import com.enterprise.cartcheckout.enums.PaymentStatus;
import com.enterprise.cartcheckout.mapper.PaymentMapper;
import com.enterprise.cartcheckout.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment APIs")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get payment by order ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentByOrderId(@PathVariable String orderId) {
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        return ResponseEntity.ok(ApiResponse.success("Payment fetched", paymentMapper.toResponse(payment), 200));
    }

    @PostMapping("/callback")
    @Operation(summary = "Webhook for payment gateway callback")
    public ResponseEntity<ApiResponse<PaymentResponse>> paymentCallback(@RequestBody PaymentCallbackRequest request) {
        Payment payment = paymentService.processPaymentCallback(request.getTransactionId(), request.getStatus());
        return ResponseEntity.ok(ApiResponse.success("Payment callback processed", paymentMapper.toResponse(payment), 200));
    }

    @PostMapping("/{paymentId}/refund")
    @Operation(summary = "Refund a payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> refundPayment(@PathVariable String paymentId) {
        Payment payment = paymentService.refundPayment(paymentId);
        return ResponseEntity.ok(ApiResponse.success("Payment refunded", paymentMapper.toResponse(payment), 200));
    }

    @Data
    public static class PaymentCallbackRequest {
        private String transactionId;
        private PaymentStatus status;
    }
}
