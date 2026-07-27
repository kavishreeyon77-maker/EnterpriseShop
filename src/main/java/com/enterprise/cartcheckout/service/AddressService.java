package com.enterprise.cartcheckout.service;

import com.enterprise.cartcheckout.entity.Address;

import java.util.List;

public interface AddressService {
    List<Address> getUserAddresses(String userId);
    Address getAddress(String addressId);
    Address addAddress(String userId, Address address);
    Address updateAddress(String addressId, Address addressDetails);
    void deleteAddress(String addressId);
}
