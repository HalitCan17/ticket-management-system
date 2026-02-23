package com.halitcan.ticket_management_system.application.ticket.dto.api;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record UpdateTicketStatusRequest(
        @NotNull(message = "Yeni durum (status) boş bırakılamaz.")
        TicketStatus status,

        UUID assigneeId
) {}