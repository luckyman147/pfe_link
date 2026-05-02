package com.pfelink.monolith.infrastructure.api;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String errorCode;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private long responseTimeMs;
    private int status;

    public static <T> ApiResponse<T> success(T data, long responseTimeMs) {
        return success(data, responseTimeMs, 200);
    }

    public static <T> ApiResponse<T> success(T data, long responseTimeMs, int status) {
        return ApiResponse.<T>builder()
                .success(true)
                .status(status)
                .data(data)
                .responseTimeMs(responseTimeMs)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> failure(String errorCode, String message, int status, long responseTimeMs) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .status(status)
                .responseTimeMs(responseTimeMs)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
