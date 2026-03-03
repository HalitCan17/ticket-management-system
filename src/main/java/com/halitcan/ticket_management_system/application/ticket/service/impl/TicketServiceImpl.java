package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.application.ticket.service.TicketService;
import com.halitcan.ticket_management_system.common.api.PaginatedData;
import com.halitcan.ticket_management_system.domain.ticket.entity.ProductEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.SlaPolicyEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.UserEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import com.halitcan.ticket_management_system.domain.ticket.exception.InvalidTicketStateException;
import com.halitcan.ticket_management_system.domain.ticket.exception.TicketNotFoundException;
import com.halitcan.ticket_management_system.infrastructure.persistence.ProductRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.SlaPolicyRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SlaPolicyRepository slaPolicyRepository;
    private static final List<TicketStatus> POOL_STATUSES = List.of(TicketStatus.NEW, TicketStatus.IN_PROGRESS);

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

    private TicketEntity createTicket(UUID requesterId, UUID productId, String title, String description, TicketPriority priority) {
        UserEntity requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + requesterId));

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Seçilen yazılım modülü/ürünü bulunamadı: " + productId));

        TicketEntity ticket = new TicketEntity();
        ticket.setPublicId(UUID.randomUUID());
        ticket.setRequester(requester);
        ticket.setProduct(product);

        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setPriority(priority);
        ticket.setStatus(TicketStatus.NEW);

        SlaPolicyEntity slaPolicy = slaPolicyRepository.findByPriority(priority)
                .orElseThrow(() -> new IllegalStateException("SLA Politikası bulunamadı: " + priority));

        Instant now = Instant.now();

        ticket.setFirstResponseDueAt(now.plus(slaPolicy.getResponseTimeHours(), ChronoUnit.HOURS));
        ticket.setResolutionDueAt(now.plus(slaPolicy.getResolutionTimeHours(), ChronoUnit.HOURS));
        ticket.setSlaBreached(false);

        return ticketRepository.save(ticket);
    }


    @Transactional(readOnly = true)
    public TicketEntity getTicketByPublicId(UUID publicId) {
        return ticketRepository.findByPublicId(publicId)
                .orElseThrow(() -> new TicketNotFoundException(publicId));
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
                t.getProduct() !=null? t.getProduct().getId() :null,
                t.getFirstResponseAt(),
                t.getFirstResponseDueAt(),
                t.getResolutionDueAt(),
                t.isSlaBreached()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketResponse> listTickets(int page, int size, TicketStatus status, TicketPriority priority) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        org.springframework.data.domain.Page<TicketEntity> entityPage = ticketRepository.findWithFilters(status, priority, pageRequest);

        List<TicketResponse> dtoList = entityPage.getContent().stream()
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

        List<TicketResponse> dtoList = entityPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedData<>(dtoList, entityPage.getNumber(), entityPage.getSize(), entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    @Override
    @Transactional
    public TicketResponse updateTicketStatus(UUID publicId, TicketStatus newStatus, UUID assigneeId) {
        TicketEntity ticket = getTicketByPublicId(publicId);

        if ((ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED)
                && (newStatus == TicketStatus.NEW || newStatus == TicketStatus.IN_PROGRESS)) {
            throw new InvalidTicketStateException(
                    "Kapanmış veya çözülmüş bir bilet, Yeni veya İşlemde statüsüne alınamaz.");
        }

        if (assigneeId != null) {
            UserEntity assignee = userRepository.findById(assigneeId)
                    .orElseThrow(() -> new EntityNotFoundException("Atanacak personel bulunamadı: " + assigneeId));
            ticket.setAssignee(assignee);
        }

        if (newStatus == TicketStatus.RESOLVED) {
            if (ticket.getAssignee() == null) {
                throw new InvalidTicketStateException(
                        "Bilet teknik bir personele atanmadan 'Çözüldü' durumuna getirilemez.");
            }
        }

        ticket.setStatus(newStatus);
        TicketEntity updatedTicket = ticketRepository.save(ticket);
        return toResponse(updatedTicket);
    }

    @Override
    @Transactional(readOnly = true)
    public PaginatedData<TicketResponse> listPoolTickets(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        org.springframework.data.domain.Page<TicketEntity> entityPage =
                ticketRepository.findPoolTickets(POOL_STATUSES, pageRequest);

        List<TicketResponse> dtoList = entityPage.getContent().stream()
                .map(this::toResponse)
                .toList();

        return new PaginatedData<>(dtoList, entityPage.getNumber(), entityPage.getSize(),
                entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    @Override
    @Transactional
    public TicketResponse claimTicket(UUID publicId, UUID assigneeId) {
        TicketEntity ticket = getTicketByPublicId(publicId);

        if (ticket.getAssignee() != null) {
            throw new InvalidTicketStateException(
                    "Bu bilet zaten başka bir personele atanmış. Sahiplenilemez.");
        }

        if (ticket.getStatus() == TicketStatus.CLOSED || ticket.getStatus() == TicketStatus.RESOLVED) {
            throw new InvalidTicketStateException(
                    "Kapatılmış veya çözülmüş biletler sahiplenilemez.");
        }

        UserEntity assignee = userRepository.findById(assigneeId)
                .orElseThrow(() -> new EntityNotFoundException("Personel bulunamadı: " + assigneeId));

        ticket.setAssignee(assignee);
        ticket.setStatus(TicketStatus.IN_PROGRESS);

        if (ticket.getFirstResponseAt() == null) {
            ticket.setFirstResponseAt(Instant.now());
        }

        return toResponse(ticketRepository.save(ticket));
    }

    @Override
    @Transactional
    public TicketResponse resolveTicket(UUID publicId, UUID agentId) {
        TicketEntity ticket = getTicketByPublicId(publicId);

        if (ticket.getStatus() != TicketStatus.IN_PROGRESS) {
            throw new InvalidTicketStateException(
                    "Sadece işlemdeki (IN_PROGRESS) biletler çözülebilir.");
        }

        if (ticket.getAssignee() == null || !ticket.getAssignee().getId().equals(agentId)) {
            throw new InvalidTicketStateException(
                    "Bu bileti sadece üzerine alan yetkili personel çözebilir.");
        }

        ticket.setStatus(TicketStatus.RESOLVED);
        Instant now = Instant.now();


        if (now.isAfter(ticket.getResolutionDueAt())) {
            ticket.setSlaBreached(true);
            log.warn("SLA İHLALİ: Bilet {} hedeflenen süreden geç çözüldü!", publicId);
        } else {
            ticket.setSlaBreached(false);
            log.info("SLA BAŞARILI: Bilet {} zamanında çözüldü.", publicId);
        }

        return toResponse(ticketRepository.save(ticket));
    }
}