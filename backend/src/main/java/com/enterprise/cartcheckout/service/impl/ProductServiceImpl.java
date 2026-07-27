package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.AppConstants;
import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Product;
import com.enterprise.cartcheckout.repository.ProductRepository;
import com.enterprise.cartcheckout.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    @Cacheable(value = AppConstants.CACHE_PRODUCTS, key = "'all'")
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .filter(p -> !p.isDeleted() && p.isActive())
                .toList();
    }

    @Override
    @Cacheable(value = AppConstants.CACHE_PRODUCTS, key = "#id")
    public Product getProductById(String id) {
        return productRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.PRODUCT_NOT_FOUND));
    }

    @Override
    public Product getProductBySku(String sku) {
        return productRepository.findBySkuAndDeletedFalse(sku)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.PRODUCT_NOT_FOUND));
    }

    @Override
    public List<Product> getProductsByCategory(String categoryId) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId).stream()
                .filter(Product::isActive)
                .toList();
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_PRODUCTS, allEntries = true)
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_PRODUCTS, allEntries = true)
    public Product updateProduct(String id, Product product) {
        Product existing = getProductById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setDiscountPrice(product.getDiscountPrice());
        existing.setImages(product.getImages());
        existing.setBrand(product.getBrand());
        existing.setStock(product.getStock());
        existing.setTags(product.getTags());
        existing.setCategoryId(product.getCategoryId());
        existing.setActive(product.isActive());
        return productRepository.save(existing);
    }

    @Override
    @CacheEvict(value = AppConstants.CACHE_PRODUCTS, allEntries = true)
    public void deleteProduct(String id) {
        Product existing = getProductById(id);
        existing.setDeleted(true);
        productRepository.save(existing);
    }
}
