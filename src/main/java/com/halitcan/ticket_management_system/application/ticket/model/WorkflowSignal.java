package com.halitcan.ticket_management_system.application.ticket.model;

public enum WorkflowSignal {
    START_PROGRESS("START_WORK"),
    RESOLVE("RESOLVE_TICKET"),
    CLOSE("CLOSE_TICKET");

    private final String signalName;

    WorkflowSignal(String signalName) {
        this.signalName = signalName;
    }

    public String getSignalName() {
        return signalName;
    }
}