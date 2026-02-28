package com.halitcan.ticket_management_system.application.ticket.dto.api;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateTicketRequest(
        @NotNull(message = "Bileti açan kullanıcı ID'si boş olamaz.") UUID requesterId,
        @NotNull(message = "Biletin hangi ürün/modül için açıldığı belirtilmelidir.") UUID productId,
        @NotBlank(message = "Başlık boş bırakılamaz.") String title,
        @NotBlank(message = "Açıklama boş bırakılamaz.") String description,
        @NotNull TicketPriority priority
) {}