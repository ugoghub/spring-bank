package com.banco.bank_system.application.transaction.port;

import com.banco.bank_system.domain.entities.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionRepositoryPort {
    void save(Transaction transaction);

    List<Transaction> findByAccountId(UUID sourceId);
}
