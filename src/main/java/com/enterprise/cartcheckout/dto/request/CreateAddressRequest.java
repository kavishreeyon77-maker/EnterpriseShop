package com.enterprise.cartcheckout.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateAddressRequest {

    @NotBlank(message = "Recipient name is required")
    private String recipientName;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String recipientPhone;

    @NotBlank(message = "Street address is required")
    private String street;

    private String addressLine2;

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Country is required")
    private String country;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid PIN code")
    private String zipCode;

    private boolean shippingAddress = true;
    private boolean billingAddress = true;
    private boolean defaultAddress = false;
}
