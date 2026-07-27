package com.enterprise.cartcheckout.events;

import com.enterprise.cartcheckout.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class NotificationEvent {
    private String userId;
    private String title;
    private String message;
    private NotificationType type;
}
