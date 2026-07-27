package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.entity.Notification;
import com.enterprise.cartcheckout.repository.NotificationRepository;
import com.enterprise.cartcheckout.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<Notification> getUserNotifications(String userId) {
        return notificationRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    public Notification markAsRead(String notificationId) {
        Notification notification = notificationRepository.findByIdAndDeletedFalse(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }
}
