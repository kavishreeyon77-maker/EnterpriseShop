package com.enterprise.cartcheckout.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConfirmCheckoutRequest {

    @NotBlank(message = "Checkout token is required")
    private String checkoutToken;

    // Payment gateway transaction ID or reference from payment gateway
    private String paymentGatewayReference;
}
