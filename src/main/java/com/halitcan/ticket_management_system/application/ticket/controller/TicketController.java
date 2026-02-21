package com.halitcan.ticket_management_system.application.ticket.controller;

import com.halitcan.ticket_management_system.application.ticket.dto.api.CreateTicketRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.TicketResponse;
import com.halitcan.ticket_management_system.application.ticket.service.impl.TicketServiceImpl;
import com.halitcan.ticket_management_system.common.api.ApiResponse;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketServiceImpl ticketServiceImpl;

    public TicketController(TicketServiceImpl ticketServiceImpl) {
        this.ticketServiceImpl = ticketServiceImpl;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<ApiResponse<TicketResponse>> create(@Valid @RequestBody CreateTicketRequest request) {
        TicketResponse created = ticketServiceImpl.create(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.ok(created, null));
    }

    // GET BY PUBLIC ID
    @GetMapping("/{publicId}")
    public ResponseEntity<ApiResponse<TicketResponse>> getByPublicId(@PathVariable UUID publicId) {
        TicketResponse ticket = ticketServiceImpl.getByPublicId(publicId);
        return ResponseEntity
                .ok(ApiResponse.ok(ticket, null));
    }
}