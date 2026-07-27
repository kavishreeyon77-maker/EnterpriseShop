package com.enterprise.cartcheckout.dto.response;

import com.enterprise.cartcheckout.enums.PaymentMethod;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CheckoutSessionResponse {
    private String id;
    private String userId;
    private String cartId;
    private String checkoutToken;
    private String shippingAddressId;
    private String billingAddressId;
    private String couponCode;
    private String shippingMethod;
    private PaymentMethod paymentMethod;
    private BigDecimal subTotal;
    private BigDecimal discount;
    private BigDecimal tax;
    private BigDecimal shipping;
    private BigDecimal grandTotal;
    private String expiresAt;
    private boolean confirmed;
    private String orderId;
}
