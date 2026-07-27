package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.CheckoutSession;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckoutSessionRepository extends MongoRepository<CheckoutSession, String> {
    Optional<CheckoutSession> findByIdAndDeletedFalse(String id);
    Optional<CheckoutSession> findByCheckoutTokenAndDeletedFalse(String token);
}
