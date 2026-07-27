package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.WishlistResponse;
import com.enterprise.cartcheckout.entity.Wishlist;
import com.enterprise.cartcheckout.mapper.WishlistMapper;
import com.enterprise.cartcheckout.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
@Tag(name = "Wishlist", description = "Wishlist APIs")
public class WishlistController {

    private final WishlistService wishlistService;
    private final WishlistMapper wishlistMapper;

    @GetMapping
    @Operation(summary = "Get current user wishlist")
    public ResponseEntity<ApiResponse<WishlistResponse>> getWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        Wishlist wishlist = wishlistService.getWishlistByUserId(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Wishlist fetched", wishlistMapper.toResponse(wishlist), 200));
    }

    @PostMapping("/{productId}")
    @Operation(summary = "Add product to wishlist")
    public ResponseEntity<ApiResponse<WishlistResponse>> addProductToWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String productId) {
        Wishlist wishlist = wishlistService.addItemToWishlist(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Product added to wishlist", wishlistMapper.toResponse(wishlist), 200));
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "Remove product from wishlist")
    public ResponseEntity<ApiResponse<WishlistResponse>> removeProductFromWishlist(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String productId) {
        Wishlist wishlist = wishlistService.removeItemFromWishlist(userDetails.getUsername(), productId);
        return ResponseEntity.ok(ApiResponse.success("Product removed from wishlist", wishlistMapper.toResponse(wishlist), 200));
    }

    @DeleteMapping
    @Operation(summary = "Clear wishlist")
    public ResponseEntity<ApiResponse<Void>> clearWishlist(@AuthenticationPrincipal UserDetails userDetails) {
        wishlistService.clearWishlist(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Wishlist cleared", null, 200));
    }
}
