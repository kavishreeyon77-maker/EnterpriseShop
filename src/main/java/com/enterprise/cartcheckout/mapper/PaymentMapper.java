package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.PaymentResponse;
import com.enterprise.cartcheckout.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
