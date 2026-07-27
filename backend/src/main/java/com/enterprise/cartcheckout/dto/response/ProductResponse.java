package com.enterprise.cartcheckout.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private String id;
    private String name;
    private String sku;
    private String description;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private List<String> images;
    private String brand;
    private Integer stock;
    private Double rating;
    private List<String> tags;
    private String categoryId;
    private String status;
    private boolean active;
}
