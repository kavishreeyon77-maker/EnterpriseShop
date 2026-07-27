package com.enterprise.cartcheckout.service;

public interface AuditService {
    void logAction(String userId, String action, String entityType, String entityId, String details, String ipAddress);
}
