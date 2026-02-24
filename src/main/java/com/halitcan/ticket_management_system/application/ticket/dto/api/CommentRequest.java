package com.halitcan.ticket_management_system.application.ticket.dto.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CommentRequest(
        @NotBlank(message = "Yorum içeriği boş olamaz")
        String content,

        @NotNull(message = "Yazar bilgisi gereklidir")
        UUID authorId,

        boolean isInternal,

        UUID parentPublicId
) {}