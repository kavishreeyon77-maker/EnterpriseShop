package com.enterprise.cartcheckout.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String email;
    private String firstName;
    private String lastName;
}
