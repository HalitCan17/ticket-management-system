package com.halitcan.ticket_management_system.domain.ticket.exception;

import com.halitcan.ticket_management_system.domain.exception.BaseBusinessException;

public class InvalidFileException extends BaseBusinessException {
    public InvalidFileException(String message) {
        super(message, "INVALID_FILE_FORMAT");
    }
}