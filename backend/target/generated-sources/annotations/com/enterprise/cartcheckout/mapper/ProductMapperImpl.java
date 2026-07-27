package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.ProductResponse;
import com.enterprise.cartcheckout.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-27T15:33:22+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class ProductMapperImpl implements ProductMapper {

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.active( product.isActive() );
        productResponse.brand( product.getBrand() );
        productResponse.categoryId( product.getCategoryId() );
        productResponse.description( product.getDescription() );
        productResponse.discountPrice( product.getDiscountPrice() );
        productResponse.id( product.getId() );
        List<String> list = product.getImages();
        if ( list != null ) {
            productResponse.images( new ArrayList<String>( list ) );
        }
        productResponse.name( product.getName() );
        productResponse.price( product.getPrice() );
        productResponse.rating( product.getRating() );
        productResponse.sku( product.getSku() );
        productResponse.status( product.getStatus() );
        productResponse.stock( product.getStock() );
        List<String> list1 = product.getTags();
        if ( list1 != null ) {
            productResponse.tags( new ArrayList<String>( list1 ) );
        }

        return productResponse.build();
    }
}
