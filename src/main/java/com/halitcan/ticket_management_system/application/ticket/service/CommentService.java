package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentResponse;
import java.util.List;
import java.util.UUID;

public interface CommentService {
    CommentResponse addComment(UUID ticketPublicId, CommentRequest request);
    List<CommentResponse> getCommentsByTicket(UUID ticketPublicId);
}