package com.enterprise.cartcheckout.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateCartItemRequest {

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Min(value = 1, message = "Minimum quantity is 1")
    private Integer quantity;
}
