package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentResponse;
import com.halitcan.ticket_management_system.application.ticket.service.CommentService;
import com.halitcan.ticket_management_system.common.exception.ResourceNotFoundException;
import com.halitcan.ticket_management_system.domain.ticket.entity.CommentEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.infrastructure.persistence.CommentRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;

    @Override
    @Transactional
    public CommentResponse addComment(UUID ticketPublicId, CommentRequest request) {
        TicketEntity ticket = ticketRepository.findByPublicId(ticketPublicId)
                .orElseThrow(() -> new ResourceNotFoundException("İlgili bilet bulunamadı: " + ticketPublicId));

        CommentEntity comment = CommentEntity.builder()
                .content(request.content())
                .authorId(request.authorId())
                .isInternal(request.isInternal())
                .ticket(ticket)
                .build();

        CommentEntity savedComment = commentRepository.save(comment);

        return mapToResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByTicket(UUID ticketPublicId) {
        return commentRepository.findByTicket_PublicIdOrderByCreatedAtAsc(ticketPublicId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse mapToResponse(CommentEntity entity) {
        return new CommentResponse(
                entity.getPublicId(),
                entity.getContent(),
                entity.getAuthorId(),
                entity.isInternal(),
                entity.getCreatedAt(),
                entity.getParent() != null ? entity.getParent().getPublicId() : null
        );
    }
}