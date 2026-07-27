package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.AddressResponse;
import com.enterprise.cartcheckout.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AddressMapper {
    AddressResponse toResponse(Address address);
}
