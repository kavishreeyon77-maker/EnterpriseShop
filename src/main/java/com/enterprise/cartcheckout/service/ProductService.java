package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(String id);
    Product getProductBySku(String sku);
    List<Product> getProductsByCategory(String categoryId);
    Product createProduct(Product product);
    Product updateProduct(String id, Product product);
    void deleteProduct(String id);
}
