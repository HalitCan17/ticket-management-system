package com.halitcan.ticket_management_system.common.exception;

import com.halitcan.ticket_management_system.domain.exception.BaseBusinessException;

public class WorkflowException extends BaseBusinessException {

    public WorkflowException(String message) {
        super(message, "WORKFLOW_ERROR");
    }

    public WorkflowException(String message, Throwable cause) {
        super(message, "WORKFLOW_ERROR");
        this.initCause(cause);
    }
}