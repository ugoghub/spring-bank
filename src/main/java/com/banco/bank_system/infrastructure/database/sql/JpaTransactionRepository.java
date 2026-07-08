package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface JpaTransactionRepository extends JpaRepository<TransactionEntity, UUID> {

    List<TransactionEntity> findByAccountId(UUID accountId);
}
