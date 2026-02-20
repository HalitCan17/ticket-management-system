package com.halitcan.ticket_management_system.common.api;

import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Instant timestamp,
        String traceId
) {
    public static <T> ApiResponse<T> ok(T data, String traceId) {
        return new ApiResponse<>(true, data, null, Instant.now(), traceId);
    }

    public static <T> ApiResponse<T> fail(ApiError error, String traceId) {
        return new ApiResponse<>(false, null, error, Instant.now(), traceId);
    }
}