package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.CartResponse;
import com.enterprise.cartcheckout.entity.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CartMapper {
    CartResponse toResponse(Cart cart);
}
