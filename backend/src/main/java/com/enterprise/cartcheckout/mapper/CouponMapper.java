package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.CouponResponse;
import com.enterprise.cartcheckout.entity.Coupon;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CouponMapper {
    CouponResponse toResponse(Coupon coupon);
}
