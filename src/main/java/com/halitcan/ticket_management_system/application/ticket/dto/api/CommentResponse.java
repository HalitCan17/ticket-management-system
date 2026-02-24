package com.halitcan.ticket_management_system.application.ticket.dto.api;

import java.time.Instant;
import java.util.UUID;

public record CommentResponse(
        UUID publicId,
        String content,
        UUID authorId,
        boolean isInternal,
        Instant createdAt,
        UUID parentPublicId
) {}