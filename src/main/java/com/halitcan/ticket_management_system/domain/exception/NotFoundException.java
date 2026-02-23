package com.halitcan.ticket_management_system.domain.exception;

public class NotFoundException extends BaseBusinessException{
    public NotFoundException(String message,String errorCode){
        super(message, errorCode);
    }
}
