package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    Page<TransactionEntity> findByAccountId(
            UUID accountId,
            Pageable pageable
    );
}
