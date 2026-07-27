package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByIdAndDeletedFalse(String id);
    Optional<Product> findBySkuAndDeletedFalse(String sku);
    List<Product> findByCategoryIdAndDeletedFalse(String categoryId);
}
