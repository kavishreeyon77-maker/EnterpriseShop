package com.enterprise.cartcheckout.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Standard API response wrapper for all endpoints.
 *
 * @param <T> the type of data payload
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private int status;
    private String path;
    @Builder.Default
    private String timestamp = LocalDateTime.now().toString();

    public static <T> ApiResponse<T> success(String message, T data, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .status(status)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int status, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .status(status)
                .path(path)
                .build();
    }

    public static <T> ApiResponse<T> error(String message, int status) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .status(status)
                .build();
    }
}
