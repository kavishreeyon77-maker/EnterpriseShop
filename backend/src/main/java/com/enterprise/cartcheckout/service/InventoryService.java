package com.enterprise.cartcheckout.service;

public interface InventoryService {
    void reserveInventory(String sku, int quantity);
    void releaseInventory(String sku, int quantity);
    void reduceInventory(String sku, int quantity);
}
