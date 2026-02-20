package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * KALICI: Controller bu methodu çağırır (DTO -> Entity -> DTO)
     */
    @Transactional
    public TicketResponse create(CreateTicketRequest request) {

        // request record ise: request.title() / request.priority() şeklinde erişilir
        TicketPriority priority = request.priority() != null ? request.priority() : TicketPriority.LOW;

        TicketEntity created = createTicket(
                request.requesterId(),
                request.title(),
                request.description(),
                priority
        );

        return toResponse(created);
    }

    /**
     * KALICI: Controller bu methodu çağırır
     */
    @Transactional(readOnly = true)
    public TicketResponse getByPublicId(UUID publicId) {
        TicketEntity ticket = getTicketByPublicId(publicId);
        return toResponse(ticket);
    }

    /**
     * Domain/business method (istersen private da yapabiliriz)
     */
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

    /**
     * Domain/business method (istersen private da yapabiliriz)
     */
    @Transactional(readOnly = true)
    public TicketEntity getTicketByPublicId(UUID publicId) {
        return ticketRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + publicId));
    }

    private TicketResponse toResponse(TicketEntity t) {
        // TicketResponse record sırası: (publicId, title, description, status, priority)
        return new TicketResponse(
                t.getPublicId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority()
        );
    }
}