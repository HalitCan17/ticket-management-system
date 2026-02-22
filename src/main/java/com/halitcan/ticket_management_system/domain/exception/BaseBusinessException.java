package com.halitcan.ticket_management_system.domain.exception;

public abstract class BaseBusinessException  extends RuntimeException{
    private final String errorCode;

    protected BaseBusinessException(String message, String errorCode)
    {
        super(message);
        this.errorCode=errorCode;
    }

    public String getErrorCode(){
        return errorCode;
    }
}
