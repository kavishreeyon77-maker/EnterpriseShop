package com.enterprise.cartcheckout.validation;

import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StockValidator {

    private final ProductService productService;

    public boolean isStockAvailable(String sku, int quantity) {
        Product product = productService.getProductBySku(sku);
        return product.getStock() >= quantity;
    }

    public void validateStock(String sku, int quantity) {
        if (!isStockAvailable(sku, quantity)) {
            throw new RuntimeException("Insufficient stock for product SKU: " + sku);
        }
    }
}
