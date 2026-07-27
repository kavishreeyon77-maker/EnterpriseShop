package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.CheckoutSession;
import com.enterprise.cartcheckout.repository.CheckoutSessionRepository;
import com.enterprise.cartcheckout.service.impl.CheckoutServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CheckoutServiceTest {

    @Mock
    private CheckoutSessionRepository checkoutSessionRepository;

    @Mock
    private CartService cartService;

    @InjectMocks
    private CheckoutServiceImpl checkoutService;

    @Test
    void testCompleteCheckout_SessionExpired() {
        CheckoutSession expiredSession = CheckoutSession.builder()
                .checkoutToken("token123")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .build();

        when(checkoutSessionRepository.findByCheckoutTokenAndDeletedFalse("token123"))
                .thenReturn(Optional.of(expiredSession));

        assertThrows(RuntimeException.class, () -> {
            checkoutService.completeCheckout("token123");
        });
    }
}
