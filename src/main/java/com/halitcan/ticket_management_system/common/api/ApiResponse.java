package com.halitcan.ticket_management_system.common.api;

import com.halitcan.ticket_management_system.common.Trace;
import java.time.Instant;

public record ApiResponse<T>(
        boolean success,
        T data,
        ApiError error,
        Instant timestamp,
        String traceId
) {
    // 1. Başarılı durumlar için dışarıdan traceId ALMIYORUZ
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, data, null, Instant.now(), Trace.id());
    }

    // 2. Hatalı durumlar için de dışarıdan traceId ALMIYORUZ
    public static <T> ApiResponse<T> fail(ApiError error) {
        return new ApiResponse<>(false, null, error, Instant.now(), Trace.id());
    }
}