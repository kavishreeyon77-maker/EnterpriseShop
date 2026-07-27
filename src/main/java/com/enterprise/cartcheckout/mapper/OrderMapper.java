package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.OrderResponse;
import com.enterprise.cartcheckout.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResponse toResponse(Order order);
}
