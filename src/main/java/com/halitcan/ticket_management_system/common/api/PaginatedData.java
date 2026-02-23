package com.halitcan.ticket_management_system.common.api;

import java.util.List;

public record PaginatedData<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages
) {}