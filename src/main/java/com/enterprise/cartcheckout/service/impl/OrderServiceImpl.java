package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.entity.User;
import com.enterprise.cartcheckout.enums.OrderStatus;
import com.enterprise.cartcheckout.events.OrderPlacedEvent;
import com.enterprise.cartcheckout.repository.OrderRepository;
import com.enterprise.cartcheckout.repository.UserRepository;
import com.enterprise.cartcheckout.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public Order createOrder(Order order) {
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        order.setOrderStatus(OrderStatus.PENDING);
        order.getOrderTimeline().add(Order.OrderStatusHistory.builder()
                .status(OrderStatus.PENDING)
                .description("Order has been placed and is awaiting payment")
                .build());
                
        Order savedOrder = orderRepository.save(order);
        
        User user = userRepository.findByIdAndDeletedFalse(savedOrder.getUserId()).orElse(null);
        if (user != null) {
            eventPublisher.publishEvent(new OrderPlacedEvent(savedOrder.getId(), savedOrder.getOrderNumber(), user.getId()));
        }
        
        return savedOrder;
    }

    @Override
    public Order getOrder(String id) {
        return orderRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ORDER_NOT_FOUND));
    }

    @Override
    public Order getOrderByNumber(String orderNumber) {
        return orderRepository.findByOrderNumberAndDeletedFalse(orderNumber)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ORDER_NOT_FOUND));
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    public Order updateOrderStatus(String id, OrderStatus status, String description) {
        Order order = getOrder(id);
        order.setOrderStatus(status);
        order.getOrderTimeline().add(Order.OrderStatusHistory.builder()
                .status(status)
                .description(description)
                .timestamp(LocalDateTime.now())
                .build());
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(String id) {
        return updateOrderStatus(id, OrderStatus.CANCELLED, "Order was cancelled");
    }
}
