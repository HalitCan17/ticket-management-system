package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.application.ticket.service.TicketService; // <-- Arayüzü import etmen gerekebilir
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;

    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        TicketPriority priority = request.priority() != null ? request.priority() : TicketPriority.LOW;

        TicketEntity created = createTicket(
                request.requesterId(),
                request.title(),
                request.description(),
                priority
        );

        return toResponse(created);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse getByPublicId(UUID publicId) {
        TicketEntity ticket = getTicketByPublicId(publicId);
        return toResponse(ticket);
    }

    @Override
    @Transactional
    public TicketEntity createTicket(UUID requesterId, String title, String description, TicketPriority priority) {
        TicketEntity ticket = new TicketEntity();

        ticket.setPublicId(UUID.randomUUID());
        ticket.setRequesterId(requesterId);

        ticket.setTitle(title);
        ticket.setDescription(description);

        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.OPEN);

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketEntity getTicketByPublicId(UUID publicId) {
        return ticketRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + publicId));
    }

    private TicketResponse toResponse(TicketEntity t) {
        return new TicketResponse(
                t.getPublicId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority()
        );
    }
}