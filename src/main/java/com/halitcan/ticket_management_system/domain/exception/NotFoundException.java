package com.halitcan.ticket_management_system.domain.exception;

public class NotFoundException extends BaseBusinessException{
    public NotFoundException(String message){
        super(message, "NOT_FOUND");
    }
}
