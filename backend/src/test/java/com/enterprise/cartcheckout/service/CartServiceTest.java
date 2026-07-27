package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Cart;
import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.repository.CartRepository;
import com.enterprise.cartcheckout.service.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductService productService;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart testCart;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        testCart = Cart.builder()
                .userId("user1")
                .subTotal(BigDecimal.ZERO)
                .build();

        testProduct = Product.builder()
                .id("prod1")
                .sku("SKU-123")
                .name("Test Product")
                .price(BigDecimal.valueOf(100))
                .taxRate(BigDecimal.valueOf(10))
                .build();
    }

    @Test
    void testGetCart() {
        when(cartRepository.findAllByUserIdAndDeletedFalse("user1")).thenReturn(java.util.List.of(testCart));
        when(cartRepository.findFirstByUserIdAndDeletedFalse("user1")).thenReturn(Optional.of(testCart));

        Cart cart = cartService.getCart("user1");

        assertNotNull(cart);
        assertEquals("user1", cart.getUserId());
        verify(cartRepository, times(1)).findFirstByUserIdAndDeletedFalse("user1");
    }

    @Test
    void testAddToCart() {
        when(cartRepository.findAllByUserIdAndDeletedFalse("user1")).thenReturn(java.util.List.of(testCart));
        when(cartRepository.findFirstByUserIdAndDeletedFalse("user1")).thenReturn(Optional.of(testCart));
        when(productService.getProductBySku("SKU-123")).thenReturn(testProduct);
        when(cartRepository.save(any(Cart.class))).thenReturn(testCart);

        Cart result = cartService.addToCart("user1", "SKU-123", 2);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(BigDecimal.valueOf(200), result.getItems().get(0).getSubtotal());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }
}
