package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    List<Notification> findByUserIdAndDeletedFalse(String userId);
    Optional<Notification> findByIdAndDeletedFalse(String id);
}
