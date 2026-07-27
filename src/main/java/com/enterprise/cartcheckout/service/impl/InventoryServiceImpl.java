package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Inventory;
import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.repository.InventoryRepository;
import com.enterprise.cartcheckout.service.InventoryService;
import com.enterprise.cartcheckout.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductService productService;

    @Override
    @Transactional
    public void reserveInventory(String sku, int quantity) {
        Product product = productService.getProductBySku(sku);
        if (product.getStock() < quantity) {
            throw new RuntimeException(ErrorMessages.OUT_OF_STOCK + ": " + product.getName());
        }
        
        product.setStock(product.getStock() - quantity);
        productService.updateProduct(product.getId(), product);
        
        Inventory inventory = inventoryRepository.findByProductSkuAndDeletedFalse(sku)
                .orElse(Inventory.builder()
                        .productSku(sku)
                        .reservedStock(0)
                        .availableStock(product.getStock())
                        .build());
                        
        inventory.setReservedStock(inventory.getReservedStock() + quantity);
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void releaseInventory(String sku, int quantity) {
        Product product = productService.getProductBySku(sku);
        product.setStock(product.getStock() + quantity);
        productService.updateProduct(product.getId(), product);
        
        Inventory inventory = inventoryRepository.findByProductSkuAndDeletedFalse(sku)
                .orElseThrow(() -> new RuntimeException("Inventory record not found"));
                
        inventory.setReservedStock(Math.max(0, inventory.getReservedStock() - quantity));
        inventoryRepository.save(inventory);
    }

    @Override
    @Transactional
    public void reduceInventory(String sku, int quantity) {
        Inventory inventory = inventoryRepository.findByProductSkuAndDeletedFalse(sku)
                .orElseThrow(() -> new RuntimeException("Inventory record not found"));
                
        inventory.setReservedStock(Math.max(0, inventory.getReservedStock() - quantity));
        inventory.setAvailableStock(Math.max(0, inventory.getAvailableStock() - quantity));
        inventoryRepository.save(inventory);
    }
}
