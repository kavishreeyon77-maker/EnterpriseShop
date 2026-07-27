package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {
    Optional<Wishlist> findByUserIdAndDeletedFalse(String userId);
    Optional<Wishlist> findByIdAndDeletedFalse(String id);
}
