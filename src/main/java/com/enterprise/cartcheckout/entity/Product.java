package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@Document(collection = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String sku;

    private String description;

    private BigDecimal price;
    
    private BigDecimal discountPrice;
    
    private List<String> images;
    
    private String brand;
    
    @Builder.Default
    private Integer stock = 0;
    
    @Builder.Default
    private Double rating = 0.0;
    
    private List<String> tags;

    @Builder.Default
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Builder.Default
    private BigDecimal discount = BigDecimal.ZERO; 

    @Indexed
    private String categoryId;

    private List<ProductVariant> variants;

    private BigDecimal weight; 
    
    private String status;

    @Builder.Default
    private boolean active = true;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ProductVariant {
        private String id; 
        private String name; 
        private BigDecimal price; 
    }
}
