package com.halitcan.ticket_management_system.application.ticket.dto;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTicketRequest {

    private String title;
    private String description;
    private TicketPriority priority;
}
