package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserIdAndDeletedFalse(String userId);
    Optional<Order> findByIdAndDeletedFalse(String id);
    Optional<Order> findByOrderNumberAndDeletedFalse(String orderNumber);
}
