package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.common.api.PaginatedData;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;

import java.util.UUID;

public interface TicketService {

    TicketResponse create(CreateTicketRequest request);

    TicketResponse getByPublicId(UUID publicId);

    TicketEntity createTicket(UUID requesterId,UUID productId, String title, String description, TicketPriority priority);

    TicketEntity getTicketByPublicId(UUID publicId);

    PaginatedData<TicketResponse> listTickets(int page, int size, TicketStatus status, TicketPriority priority);

    PaginatedData<TicketResponse> listMyTickets(UUID requesterId, int page, int size, TicketStatus status, TicketPriority priority);

    TicketResponse updateTicketStatus(UUID publicId, TicketStatus newStatus, UUID assigneeId);
}