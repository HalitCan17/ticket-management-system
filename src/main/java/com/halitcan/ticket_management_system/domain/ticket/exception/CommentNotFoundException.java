package com.halitcan.ticket_management_system.domain.ticket.exception;

import com.halitcan.ticket_management_system.domain.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException(String message) {
        super(message);
    }
}