package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.OrderResponse;
import com.enterprise.cartcheckout.entity.CheckoutSession;
import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.mapper.OrderMapper;
import com.enterprise.cartcheckout.service.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
@RequiredArgsConstructor
@Tag(name = "Checkout", description = "Checkout flow APIs")
public class CheckoutController {

    private final CheckoutService checkoutService;
    private final OrderMapper orderMapper;

    @PostMapping
    @Operation(summary = "Initiate checkout")
    public ResponseEntity<ApiResponse<CheckoutSession>> initiateCheckout(@AuthenticationPrincipal UserDetails userDetails) {
        CheckoutSession session = checkoutService.initiateCheckout(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Checkout initiated", session, 200));
    }

    @PutMapping("/{token}/address/shipping")
    @Operation(summary = "Set shipping address")
    public ResponseEntity<ApiResponse<CheckoutSession>> setShippingAddress(
            @PathVariable String token,
            @RequestBody AddressRequest request) {
        CheckoutSession session = checkoutService.updateShippingAddress(token, request.getAddressId());
        return ResponseEntity.ok(ApiResponse.success("Shipping address set", session, 200));
    }
    
    @PutMapping("/{token}/address/billing")
    @Operation(summary = "Set billing address")
    public ResponseEntity<ApiResponse<CheckoutSession>> setBillingAddress(
            @PathVariable String token,
            @RequestBody AddressRequest request) {
        CheckoutSession session = checkoutService.updateBillingAddress(token, request.getAddressId());
        return ResponseEntity.ok(ApiResponse.success("Billing address set", session, 200));
    }

    @PutMapping("/{token}/payment-method")
    @Operation(summary = "Set payment method")
    public ResponseEntity<ApiResponse<CheckoutSession>> setPaymentMethod(
            @PathVariable String token,
            @RequestBody PaymentMethodRequest request) {
        CheckoutSession session = checkoutService.setPaymentMethod(token, request.getPaymentMethod());
        return ResponseEntity.ok(ApiResponse.success("Payment method set", session, 200));
    }

    @PostMapping("/confirm")
    @Operation(summary = "Confirm and complete checkout")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmCheckout(@RequestBody ConfirmCheckoutRequest request) {
        // Based on frontend integration: POST /checkout/confirm { token, paymentMethod, addressId }
        // We will update the session in one go then complete it
        checkoutService.updateShippingAddress(request.getToken(), request.getAddressId());
        checkoutService.updateBillingAddress(request.getToken(), request.getAddressId()); // assuming same
        checkoutService.setPaymentMethod(request.getToken(), request.getPaymentMethod());
        
        Order order = checkoutService.completeCheckout(request.getToken());
        return ResponseEntity.ok(ApiResponse.success("Checkout completed successfully", orderMapper.toResponse(order), 200));
    }

    @Data
    public static class AddressRequest {
        private String addressId;
    }

    @Data
    public static class PaymentMethodRequest {
        private PaymentMethod paymentMethod;
    }
    
    @Data
    public static class ConfirmCheckoutRequest {
        private String token;
        private PaymentMethod paymentMethod;
        private String addressId;
    }
}
