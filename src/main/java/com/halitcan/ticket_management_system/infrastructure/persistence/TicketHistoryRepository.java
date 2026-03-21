package com.halitcan.ticket_management_system.infrastructure.persistence;

import com.halitcan.ticket_management_system.domain.ticket.entity.TicketHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistoryEntity, UUID> {

    List<TicketHistoryEntity> findAllByTicketIdOrderByCreatedAtDesc(UUID ticketId);
}