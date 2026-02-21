package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;

import java.util.UUID;

public interface TicketService {

    TicketResponse create(CreateTicketRequest request);

    TicketResponse getByPublicId(UUID publicId);

    TicketEntity createTicket(UUID requesterId, String title, String description, TicketPriority priority);

    TicketEntity getTicketByPublicId(UUID publicId);
}