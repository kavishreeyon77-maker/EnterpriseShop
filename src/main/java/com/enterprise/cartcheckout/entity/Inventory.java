package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productSku; // maps to product SKU or variant ID

    @Builder.Default
    private Integer availableStock = 0;

    @Builder.Default
    private Integer reservedStock = 0;

    @Builder.Default
    private Integer lowStockThreshold = 5;
}
