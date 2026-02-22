package com.halitcan.ticket_management_system.domain.ticket.exception;

import com.halitcan.ticket_management_system.domain.exception.NotFoundException;

import java.util.UUID;

public class TicketNotFoundException extends NotFoundException {
    public TicketNotFoundException(UUID publicId){
        super("Bilet Bulunamadı: " + publicId );
    }
}
