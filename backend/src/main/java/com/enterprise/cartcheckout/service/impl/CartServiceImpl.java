package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.AppConstants;
import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Cart;
import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.repository.CartRepository;
import com.enterprise.cartcheckout.service.CartService;
import com.enterprise.cartcheckout.service.CouponService;
import com.enterprise.cartcheckout.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CouponService couponService;

    @Override
    @Cacheable(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart getCart(String userId) {
        // Deduplicate: if multiple non-deleted carts exist, keep the first and soft-delete the rest
        List<Cart> allCarts = cartRepository.findAllByUserIdAndDeletedFalse(userId);
        if (allCarts.size() > 1) {
            Cart primary = allCarts.get(0);
            for (int i = 1; i < allCarts.size(); i++) {
                Cart dupe = allCarts.get(i);
                dupe.setDeleted(true);
                cartRepository.save(dupe);
            }
            return primary;
        }
        return cartRepository.findFirstByUserIdAndDeletedFalse(userId)
                .orElseGet(() -> createCart(userId));
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart addToCart(String userId, String sku, int quantity) {
        Cart cart = getCart(userId);
        Product product;
        try {
            product = productService.getProductBySku(sku);
        } catch (Exception e) {
            log.warn("Product with SKU '{}' not found, cannot add to cart", sku);
            throw new RuntimeException("Product not found with SKU: " + sku);
        }

        Optional<Cart.CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getSku().equals(sku))
                .findFirst();

        if (existingItem.isPresent()) {
            Cart.CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        } else {
            BigDecimal price = product.getDiscountPrice() != null ? product.getDiscountPrice() : product.getPrice();
            Cart.CartItem newItem = Cart.CartItem.builder()
                    .productId(product.getId())
                    .sku(product.getSku())
                    .productName(product.getName())
                    .image(product.getImages() != null && !product.getImages().isEmpty() ? product.getImages().get(0) : null)
                    .price(price)
                    .quantity(quantity)
                    .taxRate(product.getTaxRate())
                    .subtotal(price.multiply(BigDecimal.valueOf(quantity)))
                    .build();
            cart.getItems().add(newItem);
        }

        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart updateCartItem(String userId, String sku, int quantity) {
        Cart cart = getCart(userId);
        if (quantity <= 0) {
            return removeFromCart(userId, sku);
        }

        cart.getItems().stream()
                .filter(item -> item.getSku().equals(sku))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(quantity)));
                });

        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart removeFromCart(String userId, String sku) {
        Cart cart = getCart(userId);
        cart.getItems().removeIf(item -> item.getSku().equals(sku));
        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public void clearCart(String userId) {
        Cart cart = getCart(userId);
        cart.getItems().clear();
        cart.setCouponCode(null);
        recalculateCart(cart);
        cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart applyCoupon(String userId, String couponCode) {
        Cart cart = getCart(userId);
        
        recalculateCart(cart); // Reset totals before validation
        
        if (!couponService.validateCoupon(couponCode, cart.getSubTotal())) {
            throw new RuntimeException(ErrorMessages.INVALID_REQUEST + ": Coupon is not valid or minimum amount not reached");
        }
        
        cart.setCouponCode(couponCode);
        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_CART, key = "#userId")
    public Cart removeCoupon(String userId) {
        Cart cart = getCart(userId);
        cart.setCouponCode(null);
        recalculateCart(cart);
        return cartRepository.save(cart);
    }

    @Override
    public Cart mergeCarts(String guestUserId, String loggedInUserId) {
        Optional<Cart> guestCartOpt = cartRepository.findFirstByUserIdAndDeletedFalse(guestUserId);
        if (guestCartOpt.isEmpty() || guestCartOpt.get().getItems().isEmpty()) {
            return getCart(loggedInUserId);
        }

        Cart guestCart = guestCartOpt.get();
        Cart userCart = getCart(loggedInUserId);

        guestCart.getItems().forEach(guestItem -> {
            Optional<Cart.CartItem> existingItem = userCart.getItems().stream()
                    .filter(item -> item.getSku().equals(guestItem.getSku()))
                    .findFirst();

            if (existingItem.isPresent()) {
                Cart.CartItem item = existingItem.get();
                item.setQuantity(item.getQuantity() + guestItem.getQuantity());
                item.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            } else {
                userCart.getItems().add(guestItem);
            }
        });

        recalculateCart(userCart);
        cartRepository.save(userCart);
        
        guestCart.setDeleted(true);
        cartRepository.save(guestCart);

        return userCart;
    }

    private Cart createCart(String userId) {
        Cart cart = Cart.builder()
                .userId(userId)
                .expiresAt(LocalDateTime.now().plusDays(AppConstants.CART_EXPIRY_DAYS))
                .build();
        return cartRepository.save(cart);
    }

    private void recalculateCart(Cart cart) {
        BigDecimal subTotal = cart.getItems().stream()
                .map(Cart.CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setSubTotal(subTotal);

        BigDecimal discount = BigDecimal.ZERO;
        if (cart.getCouponCode() != null) {
            try {
                var coupon = couponService.getCouponByCode(cart.getCouponCode());
                if (couponService.validateCoupon(cart.getCouponCode(), subTotal)) {
                    if (coupon.getDiscountType() == com.enterprise.cartcheckout.enums.CouponType.FLAT_DISCOUNT) {
                        discount = coupon.getDiscountValue();
                    } else if (coupon.getDiscountType() == com.enterprise.cartcheckout.enums.CouponType.PERCENTAGE) {
                        discount = subTotal.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
                        if (coupon.getMaximumDiscount() != null && discount.compareTo(coupon.getMaximumDiscount()) > 0) {
                            discount = coupon.getMaximumDiscount();
                        }
                    }
                } else {
                    cart.setCouponCode(null); 
                }
            } catch (Exception e) {
                cart.setCouponCode(null);
            }
        }
        
        if (discount.compareTo(subTotal) > 0) {
            discount = subTotal;
        }

        cart.setDiscount(discount);

        BigDecimal tax = BigDecimal.ZERO;
        for (Cart.CartItem item : cart.getItems()) {
            if (item.getTaxRate() != null && item.getTaxRate().compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal itemDiscountRatio = subTotal.compareTo(BigDecimal.ZERO) > 0 ? 
                        discount.multiply(item.getSubtotal()).divide(subTotal, 2, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal taxableAmount = item.getSubtotal().subtract(itemDiscountRatio);
                tax = tax.add(taxableAmount.multiply(item.getTaxRate()).divide(BigDecimal.valueOf(100)));
            }
        }
        cart.setTax(tax);

        BigDecimal shipping = subTotal.subtract(discount).compareTo(BigDecimal.valueOf(500)) > 0 ? BigDecimal.ZERO : BigDecimal.valueOf(50);
        cart.setShippingCharge(shipping);

        cart.setGrandTotal(subTotal.subtract(discount).add(tax).add(shipping));
    }
}
