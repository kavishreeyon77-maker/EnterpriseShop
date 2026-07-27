package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.entity.Wishlist;
import com.enterprise.cartcheckout.repository.WishlistRepository;
import com.enterprise.cartcheckout.service.ProductService;
import com.enterprise.cartcheckout.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;

    @Override
    public Wishlist getWishlistByUserId(String userId) {
        return wishlistRepository.findByUserIdAndDeletedFalse(userId)
                .orElseGet(() -> createWishlist(userId));
    }

    @Override
    public Wishlist addItemToWishlist(String userId, String productId) {
        Wishlist wishlist = getWishlistByUserId(userId);
        Product product = productService.getProductById(productId);

        boolean itemExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProductId().equals(productId));

        if (!itemExists) {
            Wishlist.WishlistItem newItem = Wishlist.WishlistItem.builder()
                    .productId(product.getId())
                    .sku(product.getSku())
                    .build();
            wishlist.getItems().add(newItem);
        }

        return wishlistRepository.save(wishlist);
    }

    @Override
    public Wishlist removeItemFromWishlist(String userId, String productId) {
        Wishlist wishlist = getWishlistByUserId(userId);
        wishlist.getItems().removeIf(item -> item.getProductId().equals(productId));
        return wishlistRepository.save(wishlist);
    }

    @Override
    public void clearWishlist(String userId) {
        Wishlist wishlist = getWishlistByUserId(userId);
        wishlist.getItems().clear();
        wishlistRepository.save(wishlist);
    }

    private Wishlist createWishlist(String userId) {
        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .build();
        return wishlistRepository.save(wishlist);
    }
}
