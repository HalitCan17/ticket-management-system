package com.halitcan.ticket_management_system.application.ticket.dto;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Builder
public class TicketResponse {

    private UUID publicId;
    private String title;
    private String description;
    private TicketStatus status;
    private TicketPriority priority;
    private UUID requesterId;
    private UUID assigneeId;
    private Instant createdAt;
}
