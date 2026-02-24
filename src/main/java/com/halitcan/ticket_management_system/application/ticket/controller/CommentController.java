package com.halitcan.ticket_management_system.application.ticket.controller; // Burayı düzelttik

import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentRequest;
import com.halitcan.ticket_management_system.application.ticket.dto.api.CommentResponse;
import com.halitcan.ticket_management_system.application.ticket.service.CommentService;
import com.halitcan.ticket_management_system.common.api.ApiResponse; // ApiResponse yerin doğru mu kontrol et
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tickets/{ticketPublicId}/comments")
@Tag(name = "Yorum (Comment) Yönetimi", description = "Biletlere yorum ekleme, yanıtlama ve listeleme işlemleri")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentResponse>> addComment(
            @PathVariable UUID ticketPublicId,
            @RequestBody @Valid CommentRequest request) {

        CommentResponse response = commentService.addComment(ticketPublicId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentResponse>>> getComments(
            @PathVariable UUID ticketPublicId) {

        List<CommentResponse> comments = commentService.getCommentsByTicket(ticketPublicId);
        return ResponseEntity.ok(ApiResponse.ok(comments));
    }
}