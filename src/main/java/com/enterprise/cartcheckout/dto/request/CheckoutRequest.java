package com.enterprise.cartcheckout.dto.request;

import com.enterprise.cartcheckout.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CheckoutRequest {

    @NotBlank(message = "Shipping address ID is required")
    private String shippingAddressId;

    @NotBlank(message = "Billing address ID is required")
    private String billingAddressId;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    private String shippingMethod = "STANDARD"; // STANDARD or EXPRESS

    private String couponCode; // optional coupon to apply
}
