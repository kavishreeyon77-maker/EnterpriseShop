package com.enterprise.cartcheckout.controller;

import com.enterprise.cartcheckout.common.ApiResponse;
import com.enterprise.cartcheckout.dto.response.CouponResponse;
import com.enterprise.cartcheckout.entity.Coupon;
import com.enterprise.cartcheckout.mapper.CouponMapper;
import com.enterprise.cartcheckout.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupon")
@RequiredArgsConstructor
@Tag(name = "Coupon", description = "Coupon APIs")
public class CouponController {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @GetMapping("/{code}")
    @Operation(summary = "Get coupon details")
    public ResponseEntity<ApiResponse<CouponResponse>> getCoupon(@PathVariable String code) {
        Coupon coupon = couponService.getCouponByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Coupon fetched", couponMapper.toResponse(coupon), 200));
    }

    // Admin endpoints would ideally be in a separate AdminController, but placed here for completeness
    @PostMapping
    @Operation(summary = "Create a new coupon (Admin)")
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(@RequestBody Coupon coupon) {
        Coupon savedCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(ApiResponse.success("Coupon created", couponMapper.toResponse(savedCoupon), 200));
    }
}
