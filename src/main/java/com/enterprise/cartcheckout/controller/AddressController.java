package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.AddressResponse;
import com.enterprise.cartcheckout.entity.Address;
import com.enterprise.cartcheckout.mapper.AddressMapper;
import com.enterprise.cartcheckout.service.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
@Tag(name = "Address", description = "Address management APIs")
public class AddressController {

    private final AddressService addressService;
    private final AddressMapper addressMapper;

    @GetMapping
    @Operation(summary = "Get all user addresses")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getUserAddresses(@AuthenticationPrincipal UserDetails userDetails) {
        List<AddressResponse> addresses = addressService.getUserAddresses(userDetails.getUsername()).stream()
                .map(addressMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Addresses fetched", addresses, 200));
    }

    @PostMapping
    @Operation(summary = "Add a new address")
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Address address) {
        Address savedAddress = addressService.addAddress(userDetails.getUsername(), address);
        return ResponseEntity.ok(ApiResponse.success("Address added", addressMapper.toResponse(savedAddress), 200));
    }

    @PutMapping("/{addressId}")
    @Operation(summary = "Update an address")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable String addressId,
            @RequestBody Address address) {
        Address updatedAddress = addressService.updateAddress(addressId, address);
        return ResponseEntity.ok(ApiResponse.success("Address updated", addressMapper.toResponse(updatedAddress), 200));
    }

    @DeleteMapping("/{addressId}")
    @Operation(summary = "Delete an address")
    public ResponseEntity<ApiResponse<Void>> deleteAddress(@PathVariable String addressId) {
        addressService.deleteAddress(addressId);
        return ResponseEntity.ok(ApiResponse.success("Address deleted", null, 200));
    }
}
