package com.enterprise.cartcheckout.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDateTime;

@Document(collection = "auditLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    
    @Id
    private String id;
    
    @Indexed
    private String userId;
    
    private String action;
    
    private String entityType;
    
    private String entityId;
    
    private String details;
    
    private String ipAddress;
    
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
