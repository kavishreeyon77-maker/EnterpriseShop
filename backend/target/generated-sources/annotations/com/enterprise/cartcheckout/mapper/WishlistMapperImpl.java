package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.WishlistResponse;
import com.enterprise.cartcheckout.entity.Wishlist;
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
public class WishlistMapperImpl implements WishlistMapper {

    @Override
    public WishlistResponse toResponse(Wishlist wishlist) {
        if ( wishlist == null ) {
            return null;
        }

        WishlistResponse.WishlistResponseBuilder wishlistResponse = WishlistResponse.builder();

        wishlistResponse.id( wishlist.getId() );
        wishlistResponse.items( wishlistItemListToWishlistItemResponseList( wishlist.getItems() ) );
        wishlistResponse.userId( wishlist.getUserId() );

        return wishlistResponse.build();
    }

    protected WishlistResponse.WishlistItemResponse wishlistItemToWishlistItemResponse(Wishlist.WishlistItem wishlistItem) {
        if ( wishlistItem == null ) {
            return null;
        }

        WishlistResponse.WishlistItemResponse.WishlistItemResponseBuilder wishlistItemResponse = WishlistResponse.WishlistItemResponse.builder();

        wishlistItemResponse.addedAt( wishlistItem.getAddedAt() );
        wishlistItemResponse.productId( wishlistItem.getProductId() );
        wishlistItemResponse.sku( wishlistItem.getSku() );
        wishlistItemResponse.variantId( wishlistItem.getVariantId() );

        return wishlistItemResponse.build();
    }

    protected List<WishlistResponse.WishlistItemResponse> wishlistItemListToWishlistItemResponseList(List<Wishlist.WishlistItem> list) {
        if ( list == null ) {
            return null;
        }

        List<WishlistResponse.WishlistItemResponse> list1 = new ArrayList<WishlistResponse.WishlistItemResponse>( list.size() );
        for ( Wishlist.WishlistItem wishlistItem : list ) {
            list1.add( wishlistItemToWishlistItemResponse( wishlistItem ) );
        }

        return list1;
    }
}
