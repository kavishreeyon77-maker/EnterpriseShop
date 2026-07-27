package com.enterprise.cartcheckout.repository;

import com.enterprise.cartcheckout.entity.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends MongoRepository<Inventory, String> {
    Optional<Inventory> findByProductSkuAndDeletedFalse(String sku);
}
