package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Coupon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository extends MongoRepository<Coupon, String> {
    Optional<Coupon> findByCodeAndDeletedFalse(String code);
    Optional<Coupon> findByIdAndDeletedFalse(String id);
}
