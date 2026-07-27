package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Cart;

public interface CartService {
    Cart getCart(String userId);
    Cart addToCart(String userId, String sku, int quantity);
    Cart updateCartItem(String userId, String sku, int quantity);
    Cart removeFromCart(String userId, String sku);
    void clearCart(String userId);
    Cart applyCoupon(String userId, String couponCode);
    Cart removeCoupon(String userId);
    Cart mergeCarts(String guestUserId, String loggedInUserId);
}
