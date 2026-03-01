package com.halitcan.ticket_management_system.infrastructure.persistence;

import com.halitcan.ticket_management_system.domain.ticket.entity.SlaPolicyEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SlaPolicyRepository extends JpaRepository<SlaPolicyEntity, Long> {
    Optional<SlaPolicyEntity> findByPriority(TicketPriority priority);
}