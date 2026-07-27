package com.enterprise.cartcheckout.mapper;

import com.enterprise.cartcheckout.dto.response.AddressResponse;
import com.enterprise.cartcheckout.entity.Address;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-25T15:06:35+0530",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class AddressMapperImpl implements AddressMapper {

    @Override
    public AddressResponse toResponse(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressResponse.AddressResponseBuilder addressResponse = AddressResponse.builder();

        addressResponse.addressLine1( address.getAddressLine1() );
        addressResponse.addressLine2( address.getAddressLine2() );
        addressResponse.city( address.getCity() );
        addressResponse.country( address.getCountry() );
        addressResponse.defaultAddress( address.isDefaultAddress() );
        addressResponse.fullName( address.getFullName() );
        addressResponse.id( address.getId() );
        addressResponse.phone( address.getPhone() );
        addressResponse.postalCode( address.getPostalCode() );
        addressResponse.state( address.getState() );

        return addressResponse.build();
    }
}
