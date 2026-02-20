package com.halitcan.ticket_management_system.application.ticket.dto.api;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTicketRequest(
        @NotNull UUID requesterId,
        @NotBlank String title,
        @NotBlank String description,
        @NotNull TicketPriority priority
) {}