package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.CheckoutSession;
import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.enums.PaymentMethod;

public interface CheckoutService {
    CheckoutSession initiateCheckout(String userId);
    CheckoutSession updateShippingAddress(String token, String addressId);
    CheckoutSession updateBillingAddress(String token, String addressId);
    CheckoutSession setPaymentMethod(String token, PaymentMethod paymentMethod);
    Order completeCheckout(String token);
}
