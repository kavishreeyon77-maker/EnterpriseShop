package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.CartResponse;
import com.enterprise.cartcheckout.entity.Cart;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-25T15:06:36+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CartMapperImpl implements CartMapper {

    @Override
    public CartResponse toResponse(Cart cart) {
        if ( cart == null ) {
            return null;
        }

        CartResponse.CartResponseBuilder cartResponse = CartResponse.builder();

        cartResponse.couponCode( cart.getCouponCode() );
        cartResponse.discount( cart.getDiscount() );
        cartResponse.expiresAt( cart.getExpiresAt() );
        cartResponse.grandTotal( cart.getGrandTotal() );
        cartResponse.id( cart.getId() );
        cartResponse.items( cartItemListToCartItemResponseList( cart.getItems() ) );
        cartResponse.locked( cart.isLocked() );
        cartResponse.shippingCharge( cart.getShippingCharge() );
        cartResponse.subTotal( cart.getSubTotal() );
        cartResponse.tax( cart.getTax() );
        cartResponse.userId( cart.getUserId() );

        return cartResponse.build();
    }

    protected CartResponse.CartItemResponse cartItemToCartItemResponse(Cart.CartItem cartItem) {
        if ( cartItem == null ) {
            return null;
        }

        CartResponse.CartItemResponse.CartItemResponseBuilder cartItemResponse = CartResponse.CartItemResponse.builder();

        cartItemResponse.image( cartItem.getImage() );
        cartItemResponse.price( cartItem.getPrice() );
        cartItemResponse.productId( cartItem.getProductId() );
        cartItemResponse.productName( cartItem.getProductName() );
        cartItemResponse.quantity( cartItem.getQuantity() );
        cartItemResponse.savedForLater( cartItem.isSavedForLater() );
        cartItemResponse.sku( cartItem.getSku() );
        cartItemResponse.subtotal( cartItem.getSubtotal() );

        return cartItemResponse.build();
    }

    protected List<CartResponse.CartItemResponse> cartItemListToCartItemResponseList(List<Cart.CartItem> list) {
        if ( list == null ) {
            return null;
        }

        List<CartResponse.CartItemResponse> list1 = new ArrayList<CartResponse.CartItemResponse>( list.size() );
        for ( Cart.CartItem cartItem : list ) {
            list1.add( cartItemToCartItemResponse( cartItem ) );
        }

        return list1;
    }
}
