package com.halitcan.ticket_management_system.application.ticket.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

public interface AttachmentService {
    String uploadAttachment(UUID ticketPublicId, MultipartFile file);
}