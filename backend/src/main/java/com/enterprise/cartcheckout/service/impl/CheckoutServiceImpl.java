package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.entity.*;
import com.enterprise.cartcheckout.enums.CheckoutStatus;
import com.enterprise.cartcheckout.enums.PaymentMethod;
import com.enterprise.cartcheckout.repository.CheckoutSessionRepository;
import com.enterprise.cartcheckout.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {

    private final CheckoutSessionRepository checkoutSessionRepository;
    private final CartService cartService;
    private final AddressService addressService;
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    @Override
    @Transactional
    public CheckoutSession initiateCheckout(String userId) {
        Cart cart = cartService.getCart(userId);
        
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cannot initiate checkout with an empty cart");
        }
        
        // Reserve inventory for each item in the cart
        for (Cart.CartItem item : cart.getItems()) {
            inventoryService.reserveInventory(item.getSku(), item.getQuantity());
        }

        CheckoutSession session = CheckoutSession.builder()
                .userId(userId)
                .cartId(cart.getId())
                .checkoutToken(UUID.randomUUID().toString())
                .status(CheckoutStatus.INITIATED)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
                
        return checkoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public CheckoutSession updateShippingAddress(String token, String addressId) {
        CheckoutSession session = getSession(token);
        session.setShippingAddressId(addressId);
        return checkoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public CheckoutSession updateBillingAddress(String token, String addressId) {
        CheckoutSession session = getSession(token);
        session.setBillingAddressId(addressId);
        return checkoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public CheckoutSession setPaymentMethod(String token, PaymentMethod paymentMethod) {
        CheckoutSession session = getSession(token);
        session.setPaymentMethod(paymentMethod);
        return checkoutSessionRepository.save(session);
    }

    @Override
    @Transactional
    public Order completeCheckout(String token) {
        CheckoutSession session = getSession(token);
        
        if (session.getShippingAddressId() == null) {
            throw new RuntimeException("Shipping address is required to complete checkout");
        }
        if (session.getPaymentMethod() == null) {
            throw new RuntimeException("Payment method is required to complete checkout");
        }

        Cart cart = cartService.getCart(session.getUserId());
        Address shippingAddress = addressService.getAddress(session.getShippingAddressId());
        Address billingAddress = session.getBillingAddressId() != null ? 
                addressService.getAddress(session.getBillingAddressId()) : shippingAddress;

        Order order = Order.builder()
                .userId(session.getUserId())
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .addressId(shippingAddress.getId())
                .subTotal(cart.getSubTotal())
                .discount(cart.getDiscount())
                .tax(cart.getTax())
                .shippingCharge(cart.getShippingCharge())
                .grandTotal(cart.getGrandTotal())
                .couponCode(cart.getCouponCode())
                .paymentMethod(session.getPaymentMethod())
                .items(cart.getItems().stream().map(this::mapCartItemToOrderItem).collect(Collectors.toList()))
                .build();

        Order savedOrder = orderService.createOrder(order);
        
        // Reduce inventory
        for (Order.OrderItem item : savedOrder.getItems()) {
            inventoryService.reduceInventory(item.getSku(), item.getQuantity());
        }

        // Generate Payment
        Payment payment = paymentService.initiatePayment(savedOrder.getId(), session.getPaymentMethod());
        savedOrder.setPaymentId(payment.getId());
        orderService.updateOrderStatus(savedOrder.getId(), savedOrder.getOrderStatus(), "Payment Initiated");
        
        // Clear Cart
        cartService.clearCart(session.getUserId());
        
        // Mark session complete
        session.setStatus(CheckoutStatus.COMPLETED);
        checkoutSessionRepository.save(session);

        return savedOrder;
    }

    private CheckoutSession getSession(String token) {
        CheckoutSession session = checkoutSessionRepository.findByCheckoutTokenAndDeletedFalse(token)
                .orElseThrow(() -> new RuntimeException("Checkout session not found"));
                
        if (session.getExpiresAt().isBefore(LocalDateTime.now())) {
            session.setStatus(CheckoutStatus.EXPIRED);
            checkoutSessionRepository.save(session);
            throw new RuntimeException("Checkout session expired");
        }
        
        return session;
    }

    private Order.OrderItem mapCartItemToOrderItem(Cart.CartItem item) {
        return Order.OrderItem.builder()
                .productId(item.getProductId())
                .sku(item.getSku())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .taxRate(item.getTaxRate())
                .build();
    }
}
