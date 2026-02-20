package com.halitcan.ticket_management_system.common.api;

import  java.util.List;

public record ApiError(
        String code,          // örn: VALIDATION_ERROR, TICKET_NOT_FOUND
        String message,       // kullanıcıya/FE’ye gidecek kısa mesaj
        List<ApiFieldError> fields, // validation için
        String detail
) {
    public static ApiError of(String code, String message) {
        return new ApiError(code, message, null, null);
    }

    public static ApiError validation(List<ApiFieldError> fields) {
        return new ApiError("VALIDATION_ERROR", "Validation failed", fields, null);
    }
}
