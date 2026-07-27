package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmailAndDeletedFalse(String email);
    Optional<User> findByIdAndDeletedFalse(String id);
    Boolean existsByEmailAndDeletedFalse(String email);
}
