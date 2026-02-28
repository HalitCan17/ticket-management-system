package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.application.ticket.service.TicketService;
import com.halitcan.ticket_management_system.common.api.PaginatedData;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.UserEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import com.halitcan.ticket_management_system.infrastructure.persistence.ProductRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public TicketResponse create(CreateTicketRequest request) {
        TicketPriority priority = request.priority() != null ? request.priority() : TicketPriority.LOW;

        TicketEntity created = createTicket(
                request.requesterId(),
                request.productId(),
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
    public TicketEntity createTicket(UUID requesterId, UUID productId, String title, String description, TicketPriority priority) {
        UserEntity requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + requesterId));

        com.halitcan.ticket_management_system.domain.ticket.entity.ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Seçilen yazılım modülü/ürünü bulunamadı: " + productId));

        TicketEntity ticket = new TicketEntity();
        ticket.setPublicId(UUID.randomUUID());
        ticket.setRequester(requester);
        ticket.setProduct(product);

        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.NEW);

        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional(readOnly = true)
    public TicketEntity getTicketByPublicId(UUID publicId) {
        return ticketRepository.findByPublicId(publicId)
                .orElseThrow(() -> new com.halitcan.ticket_management_system.domain.ticket.exception.TicketNotFoundException(publicId));
    }

    private TicketResponse toResponse(TicketEntity t) {
        return new TicketResponse(
                t.getPublicId(),
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getPriority(),
                t.getCreatedAt(),
                t.getAssignee() != null ? t.getAssignee().getId() : null,
                t.getProduct() !=null? t.getProduct().getId() :null
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketResponse> listTickets(int page, int size, TicketStatus status, TicketPriority priority) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        org.springframework.data.domain.Page<TicketEntity> entityPage = ticketRepository.findWithFilters(status, priority, pageRequest);

        java.util.List<TicketResponse> dtoList = entityPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedData<>(dtoList, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketResponse> listMyTickets(UUID requesterId, int page, int size, TicketStatus status, TicketPriority priority) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        org.springframework.data.domain.Page<TicketEntity> entityPage =
                ticketRepository.findByRequesterIdWithFilters(requesterId, status, priority, pageRequest);

        java.util.List<TicketResponse> dtoList = entityPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedData<>(dtoList, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    @Override
    @Transactional
    public TicketResponse updateTicketStatus(UUID publicId, TicketStatus newStatus, UUID assigneeId) {
        TicketEntity ticket = getTicketByPublicId(publicId);

        if (ticket.getStatus() == TicketStatus.CLOSED) {
            throw new com.halitcan.ticket_management_system.domain.ticket.exception.InvalidTicketStateException(
                    "Kapatılmış bir bilet üzerinde işlem yapılamaz.");
        }

        if (assigneeId != null) {
            UserEntity assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new EntityNotFoundException("Atanacak personel bulunamadı: " + assigneeId));
            ticket.setAssignee(assignee);
        }

        if (newStatus == TicketStatus.RESOLVED) {
            if (ticket.getAssignee() == null) {
                throw new com.halitcan.ticket_management_system.domain.ticket.exception.InvalidTicketStateException(
                        "Bilet teknik bir personele atanmadan 'Çözüldü' durumuna getirilemez.");
            }
        }

        ticket.setStatus(newStatus);
        TicketEntity updatedTicket = ticketRepository.save(ticket);
        return toResponse(updatedTicket);
    }
}