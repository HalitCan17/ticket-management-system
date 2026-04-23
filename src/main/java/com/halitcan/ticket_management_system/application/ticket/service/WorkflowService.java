package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.application.ticket.model.WorkflowSignal;

public interface WorkflowService {

    Long startTicketProcess(String ticketPublicId, String ownerEmail);

    void signalProcess(Long processInstanceId, WorkflowSignal signal);
}