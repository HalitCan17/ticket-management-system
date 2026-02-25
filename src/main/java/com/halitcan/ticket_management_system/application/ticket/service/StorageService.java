package com.halitcan.ticket_management_system.application.ticket.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file);
}