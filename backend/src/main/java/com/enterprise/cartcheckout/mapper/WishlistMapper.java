package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.WishlistResponse;
import com.enterprise.cartcheckout.entity.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WishlistMapper {
    WishlistResponse toResponse(Wishlist wishlist);
}
