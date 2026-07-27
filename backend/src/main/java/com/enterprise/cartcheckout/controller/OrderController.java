package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.OrderResponse;
import com.enterprise.cartcheckout.entity.Order;
import com.enterprise.cartcheckout.enums.OrderStatus;
import com.enterprise.cartcheckout.mapper.OrderMapper;
import com.enterprise.cartcheckout.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Order", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @GetMapping
    @Operation(summary = "Get current user orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getUserOrders(@AuthenticationPrincipal UserDetails userDetails) {
        List<OrderResponse> orders = orderService.getUserOrders(userDetails.getUsername()).stream()
                .map(orderMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Orders fetched", orders, 200));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order details")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable String id) {
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order fetched", orderMapper.toResponse(order), 200));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByNumber(@PathVariable String orderNumber) {
        Order order = orderService.getOrderByNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success("Order fetched", orderMapper.toResponse(order), 200));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable String id) {
        Order order = orderService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Order cancelled", orderMapper.toResponse(order), 200));
    }
}
