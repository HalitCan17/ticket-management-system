package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.service.StorageService;
import com.halitcan.ticket_management_system.domain.ticket.entity.AttachmentEntity;
import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.exception.TicketNotFoundException;
import com.halitcan.ticket_management_system.infrastructure.persistence.AttachmentRepository;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import com.halitcan.ticket_management_system.application.ticket.service.AttachmentService;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final StorageService storageService;
    private final AttachmentRepository attachmentRepository;
    private final TicketRepository ticketRepository;

    @Transactional
    public String uploadAttachment(UUID ticketPublicId, MultipartFile file) {
        // 1. Bilet var mı kontrol et
        TicketEntity ticket = ticketRepository.findByPublicId(ticketPublicId)
                .orElseThrow(() -> new TicketNotFoundException(ticketPublicId));

        // 2. Dosyayı diske kaydet ve yeni adını al
        String storedFileName = storageService.store(file);

        // 3. Veritabanına kayıt at
        AttachmentEntity attachment = AttachmentEntity.builder()
                .originalFileName(file.getOriginalFilename())
                .storedFileName(storedFileName)
                .filePath("uploads/tickets/" + storedFileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .ticket(ticket)
                .build();

        attachmentRepository.save(attachment);

        return "Dosya başarıyla yüklendi: " + file.getOriginalFilename();
    }
}




