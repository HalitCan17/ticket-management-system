package com.halitcan.ticket_management_system.application.ticket.controller;

import com.halitcan.ticket_management_system.application.ticket.service.AttachmentService;
import com.halitcan.ticket_management_system.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets/{ticketPublicId}/attachments")
@RequiredArgsConstructor
@Tag(name = "Dosya (Attachment) Yönetimi", description = "Biletlere dosya/ekran görüntüsü yükleme işlemleri")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Bilete Dosya Yükle", description = "Sadece png, jpg, pdf, txt ve log formatlarına izin verilir.")
    public ResponseEntity<ApiResponse<String>> uploadFile(
            @PathVariable UUID ticketPublicId,
            @RequestParam("file") MultipartFile file) {

        String result = attachmentService.uploadAttachment(ticketPublicId, file);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }
}