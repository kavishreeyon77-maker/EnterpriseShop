package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.CouponResponse;
import com.enterprise.cartcheckout.entity.Coupon;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-27T15:33:22+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class CouponMapperImpl implements CouponMapper {

    @Override
    public CouponResponse toResponse(Coupon coupon) {
        if ( coupon == null ) {
            return null;
        }

        CouponResponse.CouponResponseBuilder couponResponse = CouponResponse.builder();

        couponResponse.active( coupon.isActive() );
        couponResponse.code( coupon.getCode() );
        couponResponse.discountType( coupon.getDiscountType() );
        couponResponse.discountValue( coupon.getDiscountValue() );
        couponResponse.expiryDate( coupon.getExpiryDate() );
        couponResponse.id( coupon.getId() );
        couponResponse.maximumDiscount( coupon.getMaximumDiscount() );
        couponResponse.minimumAmount( coupon.getMinimumAmount() );
        couponResponse.startDate( coupon.getStartDate() );

        return couponResponse.build();
    }
}
