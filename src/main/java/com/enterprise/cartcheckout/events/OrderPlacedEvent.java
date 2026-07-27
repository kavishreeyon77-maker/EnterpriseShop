package com.enterprise.cartcheckout.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderPlacedEvent {
    private String orderId;
    private String orderNumber;
    private String userId;
}
