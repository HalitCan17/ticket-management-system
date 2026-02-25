package com.halitcan.ticket_management_system.application.ticket.service.impl;

import com.halitcan.ticket_management_system.application.ticket.service.StorageService;
import com.halitcan.ticket_management_system.domain.ticket.exception.InvalidFileException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class FileSystemStorageServiceImpl implements StorageService {

    private final Path rootLocation;
    private final List<String> allowedExtensions = Arrays.asList("png", "jpg", "jpeg", "pdf", "txt", "log");

    public FileSystemStorageServiceImpl(@Value("${app.storage.upload-dir}") String uploadDir) {
        this.rootLocation = Paths.get(uploadDir);
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Yükleme klasörü oluşturulamadı!", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InvalidFileException("Boş dosya yüklenemez.");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = getFileExtension(originalFilename);

        if (!allowedExtensions.contains(extension.toLowerCase())) {
            throw new InvalidFileException("Geçersiz dosya formatı. İzin verilenler: " + allowedExtensions);
        }

        String storedFileName = UUID.randomUUID().toString() + "." + extension;

        //güvenlik kontrolü-Dosyanın başka yere kaydedilmesini engeller
        try {
            Path destinationFile = this.rootLocation.resolve(Paths.get(storedFileName)).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new InvalidFileException("Dosya mevcut dizinin dışına kaydedilemez.");
            }
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);
            return storedFileName;
        } catch (IOException e) {
            throw new RuntimeException("Dosya kaydedilirken hata oluştu: " + storedFileName, e);
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf(".") == -1) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}