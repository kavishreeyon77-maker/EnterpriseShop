package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Wishlist;

public interface WishlistService {
    Wishlist getWishlistByUserId(String userId);
    Wishlist addItemToWishlist(String userId, String productId);
    Wishlist removeItemFromWishlist(String userId, String productId);
    void clearWishlist(String userId);
}
