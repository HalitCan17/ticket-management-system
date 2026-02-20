package com.halitcan.ticket_management_system.application.ticket.dto.api;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;

import java.util.UUID;

public record TicketResponse(
        UUID publicId,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority
) {}