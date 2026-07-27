package com.enterprise.cartcheckout.constants;

public final class AppConstants {
    
    private AppConstants() {}
    
    public static final String CACHE_PRODUCTS = "products";
    public static final String CACHE_COUPONS = "coupons";
    public static final String CACHE_CART = "cart";
    
    public static final String EXCHANGE_ORDERS = "orders.exchange";
    public static final String EXCHANGE_PAYMENTS = "payments.exchange";
    public static final String EXCHANGE_NOTIFICATIONS = "notifications.exchange";
    
    public static final String QUEUE_ORDERS = "orders.queue";
    public static final String QUEUE_NOTIFICATIONS = "notifications.queue";
    
    public static final String ROUTING_KEY_ORDER_PLACED = "order.placed";
    public static final String ROUTING_KEY_PAYMENT_SUCCESS = "payment.success";
    public static final String ROUTING_KEY_NOTIFICATION_SEND = "notification.send";
    
    public static final long CART_EXPIRY_DAYS = 7;
}
