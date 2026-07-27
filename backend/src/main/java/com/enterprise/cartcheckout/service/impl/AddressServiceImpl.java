package com.enterprise.cartcheckout.service.impl;

import com.enterprise.cartcheckout.constants.ErrorMessages;
import com.enterprise.cartcheckout.entity.Address;
import com.enterprise.cartcheckout.repository.AddressRepository;
import com.enterprise.cartcheckout.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    @Override
    public List<Address> getUserAddresses(String userId) {
        return addressRepository.findByUserIdAndDeletedFalse(userId);
    }

    @Override
    public Address getAddress(String addressId) {
        return addressRepository.findByIdAndDeletedFalse(addressId)
                .orElseThrow(() -> new RuntimeException(ErrorMessages.ADDRESS_NOT_FOUND));
    }

    @Override
    @Transactional
    public Address addAddress(String userId, Address address) {
        address.setUserId(userId);
        if (address.isDefaultAddress()) {
            resetDefaultAddress(userId);
        }
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address updateAddress(String addressId, Address addressDetails) {
        Address address = getAddress(addressId);
        
        address.setFullName(addressDetails.getFullName());
        address.setPhone(addressDetails.getPhone());
        address.setAddressLine1(addressDetails.getAddressLine1());
        address.setAddressLine2(addressDetails.getAddressLine2());
        address.setCity(addressDetails.getCity());
        address.setState(addressDetails.getState());
        address.setCountry(addressDetails.getCountry());
        address.setPostalCode(addressDetails.getPostalCode());
        
        if (addressDetails.isDefaultAddress() && !address.isDefaultAddress()) {
            resetDefaultAddress(address.getUserId());
            address.setDefaultAddress(true);
        }
        
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(String addressId) {
        Address address = getAddress(addressId);
        address.setDeleted(true);
        addressRepository.save(address);
    }

    private void resetDefaultAddress(String userId) {
        List<Address> addresses = getUserAddresses(userId);
        addresses.stream()
                .filter(Address::isDefaultAddress)
                .forEach(a -> {
                    a.setDefaultAddress(false);
                    addressRepository.save(a);
                });
    }
}
