package com.halitcan.ticket_management_system.common.api;

public record ApiFieldError(
        String field,
        String message
) {}