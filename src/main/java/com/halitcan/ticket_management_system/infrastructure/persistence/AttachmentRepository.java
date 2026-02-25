package com.halitcan.ticket_management_system.infrastructure.persistence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.halitcan.ticket_management_system.domain.ticket.entity.AttachmentEntity;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
}