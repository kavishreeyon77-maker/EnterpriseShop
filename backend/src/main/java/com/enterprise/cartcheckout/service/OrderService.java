package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    Order createOrder(Order order);
    Order getOrder(String id);
    Order getOrderByNumber(String orderNumber);
    List<Order> getUserOrders(String userId);
    Order updateOrderStatus(String id, OrderStatus status, String description);
    Order cancelOrder(String id);
}
