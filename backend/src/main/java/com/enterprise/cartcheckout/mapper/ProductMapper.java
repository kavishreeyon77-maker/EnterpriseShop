package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.ProductResponse;
import com.enterprise.cartcheckout.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {
    ProductResponse toResponse(Product product);
}
