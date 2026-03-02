package com.halitcan.ticket_management_system.application.ticket.dto.api;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;

import java.time.Instant;
import java.util.UUID;

public record TicketResponse(
        UUID publicId,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        Instant createdAt,
        UUID assigneeId,
        UUID productId,
        Instant firstResponseAt,
        Instant firstResponseDueAt,
        Instant resolutionDueAt,
        Boolean slaBreached
) {}