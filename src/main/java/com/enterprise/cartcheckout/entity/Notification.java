package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification extends BaseEntity {

    @Id
    private String id;

    @Indexed
    private String userId; // User ID or Email, system alert if null/empty

    private String title;

    private String message;

    @Builder.Default
    private boolean read = false;

    private String type; // ORDER, LOW_STOCK, COUPON, PAYMENT
}
