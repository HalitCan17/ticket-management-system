package com.halitcan.ticket_management_system.application.ticket.service;

import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.infrastructure.persistence.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SlaTrackerService {

    private final TicketRepository ticketRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void checkSlaBreaches() {
        log.info("SLA Bekçisi çalışıyor... Geciken biletler kontrol ediliyor.");

        Instant now = Instant.now();
        List<TicketEntity> breachedTickets = ticketRepository.findTicketsWithSlaBreach(now);

        if (breachedTickets.isEmpty()) {
            return;
        }

        for (TicketEntity ticket : breachedTickets) {
            ticket.setSlaBreached(true);
            ticketRepository.save(ticket);

            log.error("🚨 SLA İHLALİ ALARMI: '{}' başlıklı biletin (ID: {}) çözüm süresi doldu! Lütfen acil müdahale edin.",
                    ticket.getTitle(), ticket.getPublicId());
        }
    }
}