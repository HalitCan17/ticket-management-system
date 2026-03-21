package com.halitcan.ticket_management_system.domain.ticket.entity;

import com.halitcan.ticket_management_system.domain.ticket.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "ticket_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private TicketEntity ticket;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_status")
    private TicketStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status")
    private TicketStatus newStatus;

    @Column(name = "old_assignee_id")
    private UUID oldAssigneeId;

    @Column(name = "new_assignee_id")
    private UUID newAssigneeId;

    @Column(name = "changed_by_id", nullable = false)
    private UUID changedById;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }
}