package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Optional<Cart> findFirstByUserIdAndDeletedFalse(String userId);
    Optional<Cart> findByIdAndDeletedFalse(String id);
    List<Cart> findAllByUserIdAndDeletedFalse(String userId);
}
