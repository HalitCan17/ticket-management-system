package com.halitcan.ticket_management_system.infrastructure.persistence;

import com.halitcan.ticket_management_system.domain.ticket.entity.TicketEntity;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketPriority;
import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface TicketRepository extends JpaRepository<TicketEntity, Long> {

    Optional<TicketEntity> findByPublicId(UUID publicId);

    @Query("SELECT t FROM TicketEntity t WHERE " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority)")
    Page<TicketEntity> findWithFilters(@Param("status") TicketStatus status,
                                       @Param("priority") TicketPriority priority,
                                       Pageable pageable);

    @Query("SELECT t FROM TicketEntity t WHERE t.requester.id = :requesterId AND " +
            "(:status IS NULL OR t.status = :status) AND " +
            "(:priority IS NULL OR t.priority = :priority)")
    Page<TicketEntity> findByRequesterIdWithFilters(@Param("requesterId") UUID requesterId,
                                                    @Param("status") TicketStatus status,
                                                    @Param("priority") TicketPriority priority,
                                                    Pageable pageable);
}