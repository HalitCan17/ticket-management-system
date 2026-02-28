package com.halitcan.ticket_management_system.common.filter;

import com.halitcan.ticket_management_system.common.Trace;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TraceFilter implements Filter {

    private static final Logger log = LogManager.getLogger(TraceFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        try {
            String traceId = Trace.id();

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setHeader("X-Trace-Id", traceId);
            log.info("TraceFilter triggered, traceId={}", traceId);

            chain.doFilter(request, response);

        } finally {
            Trace.clear();
        }
    }
}