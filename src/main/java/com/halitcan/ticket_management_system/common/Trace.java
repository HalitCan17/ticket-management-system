package com.halitcan.ticket_management_system.common;

import org.apache.logging.log4j.ThreadContext;

import java.util.UUID;

public final class Trace {

    private Trace() {}

    public static String id() {
        String traceId = ThreadContext.get("traceId");

        if (traceId == null) {
            traceId = UUID.randomUUID().toString();
            ThreadContext.put("traceId", traceId);
        }

        return traceId;
    }

    public static void clear() {
        ThreadContext.clearAll();
    }
}