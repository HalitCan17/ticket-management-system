package com.halitcan.ticket_management_system.application.ticket.controller;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.application.ticket.dto.api.UpdateTicketStatusRequest;
import com.halitcan.ticket_management_system.application.ticket.service.impl.TicketServiceImpl;
import com.halitcan.ticket_management_system.common.api.ApiResponse;
import com.halitcan.ticket_management_system.common.api.PaginatedData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
@Tag(name = "Talep (Ticket) Yönetimi", description = "Bilet oluşturma, listeleme ve durum güncelleme işlemleri")
public class TicketController {

    private final TicketServiceImpl ticketServiceImpl;

    public TicketController(TicketServiceImpl ticketServiceImpl) {
        this.ticketServiceImpl = ticketServiceImpl;
    }

    @PostMapping
    @Operation(summary = "Yeni Bilet Oluştur", description = "Sisteme yeni bir destek bileti kaydeder.")
    public ResponseEntity<ApiResponse<TicketResponse>> create(@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse created = ticketServiceImpl.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(created));
    }

    @GetMapping("/{publicId}")
    @Operation(summary = "Bilet Detayı Getir", description = "Verilen benzersiz ID'ye (UUID) göre biletin detaylarını getirir.")
    public ResponseEntity<ApiResponse<TicketResponse>> getByPublicId(@PathVariable UUID publicId) {
        TicketResponse ticket = ticketServiceImpl.getByPublicId(publicId);
        return ResponseEntity.ok(ApiResponse.ok(ticket));
    }

    @GetMapping
    @Operation(summary = "Tüm Biletleri Listele", description = "Sistemdeki tüm biletleri sayfalama ve filtreleme destekli olarak listeler. (Sadece Yetkililer)")
    public ResponseEntity<ApiResponse<PaginatedData<TicketResponse>>> listTickets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus status,
            @RequestParam(required = false) com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority priority
    ) {
        PaginatedData<TicketResponse> result = ticketServiceImpl.listTickets(page, size, status, priority);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/my")
    @Operation(summary = "Kendi Biletlerimi Listele", description = "Sisteme giriş yapmış müşterinin kendi oluşturduğu biletleri listeler.")
    public ResponseEntity<ApiResponse<PaginatedData<TicketResponse>>> getMyTickets(
            @RequestParam UUID requesterId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus status,
            @RequestParam(required = false) com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority priority
    ) {

        PaginatedData<TicketResponse> result = ticketServiceImpl.listMyTickets(requesterId, page, size, status, priority);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PatchMapping("/{publicId}/status")
    @Operation(summary = "Bilet Durumunu Güncelle", description = "Biletin durumunu (Status) ve atanmış kişisini (Assignee) iş kurallarına göre günceller.")
    public ResponseEntity<ApiResponse<TicketResponse>> updateStatus(
            @PathVariable UUID publicId,
            @Valid @RequestBody UpdateTicketStatusRequest request) {

        TicketResponse updatedTicket = ticketServiceImpl.updateTicketStatus(
                publicId,
                request.status(),
                request.assigneeId()
        );

        return ResponseEntity.ok(ApiResponse.ok(updatedTicket));
    }
}