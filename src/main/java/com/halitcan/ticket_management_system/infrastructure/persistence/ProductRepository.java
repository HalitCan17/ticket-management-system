package com.halitcan.ticket_management_system.infrastructure.persistence;

import com.halitcan.ticket_management_system.domain.ticket.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
}