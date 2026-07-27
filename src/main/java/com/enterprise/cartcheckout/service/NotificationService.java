package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getUserNotifications(String userId);
    Notification markAsRead(String notificationId);
}
