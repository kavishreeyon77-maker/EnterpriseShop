package com.enterprise.cartcheckout.entity;

import com.enterprise.cartcheckout.common.BaseEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "wishlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist extends BaseEntity {

    @Id
    private String id;

    @Indexed(unique = true)
    private String userId; // User Email or ID

    @Builder.Default
    private List<WishlistItem> items = new ArrayList<>();

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class WishlistItem {
        private String productId;
        private String variantId;
        private String sku;
        
        @Builder.Default
        private LocalDateTime addedAt = LocalDateTime.now();
    }
}
