package com.halitcan.ticket_management_system.application.ticket.service;

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
     * V1 çekirdeği: Ticket oluşturma
     * Şimdilik DTO yok; bir sonraki adımda DTO ekleyip method imzasını iyileştireceğiz.
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

    @Transactional(readOnly = true)
    public TicketEntity getTicketByPublicId(UUID publicId) {
        return ticketRepository.findByPublicId(publicId)
                .orElseThrow(() -> new IllegalArgumentException("Ticket not found: " + publicId));
    }
}
