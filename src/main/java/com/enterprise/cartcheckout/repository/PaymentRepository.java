package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {
    Optional<Payment> findByOrderIdAndDeletedFalse(String orderId);
    Optional<Payment> findByIdAndDeletedFalse(String id);
    Optional<Payment> findByTransactionIdAndDeletedFalse(String transactionId);
}
