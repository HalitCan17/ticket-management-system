package com.halitcan.ticket_management_system.domain.ticket.exception;

import com.halitcan.ticket_management_system.domain.exception.BaseBusinessException;

public class InvalidTicketStateException extends BaseBusinessException {
    public InvalidTicketStateException(String message)
    {
        super(message,"INVALID_TICKET_STATE");
    }
}
