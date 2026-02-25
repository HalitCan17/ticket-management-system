package com.halitcan.ticket_management_system.domain.ticket.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttachmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID publicId;

    // Kullanıcının yüklediği orijinal dosya adı
    @Column(nullable = false)
    private String originalFileName;

    // Diske kaydederken vereceğimiz benzersiz ad
    @Column(nullable = false)
    private String storedFileName;

    // Dosyanın diskteki tam yolu
    @Column(nullable = false)
    private String filePath;

    // Dosya tipi (örnek: image/png, text/plain)
    @Column(nullable = false)
    private String fileType;

    // Dosya boyutu
    @Column(nullable = false)
    private Long fileSize;

    // Bu dosya hangi bilete ait?
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.publicId = UUID.randomUUID();
        this.createdAt = Instant.now();
    }
}