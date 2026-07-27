package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.CartResponse;
import com.enterprise.cartcheckout.entity.Cart;
import com.enterprise.cartcheckout.mapper.CartMapper;
import com.enterprise.cartcheckout.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Shopping cart APIs")
public class CartController {

    private final CartService cartService;
    private final CartMapper cartMapper;

    @GetMapping
    @Operation(summary = "Get current user cart")
    public ResponseEntity<ApiResponse<CartResponse>> getCart(@AuthenticationPrincipal UserDetails userDetails) {
        Cart cart = cartService.getCart(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Cart fetched", cartMapper.toResponse(cart), 200));
    }

    @PostMapping("/items")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<ApiResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddToCartRequest request) {
        Cart cart = cartService.addToCart(userDetails.getUsername(), request.getSku(), request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Item added to cart", cartMapper.toResponse(cart), 200));
    }

    @PutMapping("/items/{sku}")
    @Operation(summary = "Update cart item quantity")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String sku,
            @RequestBody UpdateCartItemRequest request) {
        Cart cart = cartService.updateCartItem(userDetails.getUsername(), sku, request.getQuantity());
        return ResponseEntity.ok(ApiResponse.success("Cart item updated", cartMapper.toResponse(cart), 200));
    }

    @DeleteMapping("/items/{sku}")
    @Operation(summary = "Remove item from cart")
    public ResponseEntity<ApiResponse<CartResponse>> removeFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String sku) {
        Cart cart = cartService.removeFromCart(userDetails.getUsername(), sku);
        return ResponseEntity.ok(ApiResponse.success("Item removed from cart", cartMapper.toResponse(cart), 200));
    }

    @DeleteMapping
    @Operation(summary = "Clear cart")
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal UserDetails userDetails) {
        cartService.clearCart(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Cart cleared", null, 200));
    }

    @PostMapping("/coupon/apply")
    @Operation(summary = "Apply coupon to cart")
    public ResponseEntity<ApiResponse<CartResponse>> applyCoupon(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ApplyCouponRequest request) {
        Cart cart = cartService.applyCoupon(userDetails.getUsername(), request.getCouponCode());
        return ResponseEntity.ok(ApiResponse.success("Coupon applied", cartMapper.toResponse(cart), 200));
    }

    @DeleteMapping("/coupon")
    @Operation(summary = "Remove coupon from cart")
    public ResponseEntity<ApiResponse<CartResponse>> removeCoupon(@AuthenticationPrincipal UserDetails userDetails) {
        Cart cart = cartService.removeCoupon(userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Coupon removed", cartMapper.toResponse(cart), 200));
    }

    @Data
    public static class AddToCartRequest {
        private String sku;
        private int quantity;
    }

    @Data
    public static class UpdateCartItemRequest {
        private int quantity;
    }

    @Data
    public static class ApplyCouponRequest {
        private String couponCode;
    }
}
