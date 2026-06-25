package com.banco.bank_system.application.transfer.port;

import com.banco.bank_system.domain.entities.Transaction;

import java.util.UUID;

public interface TransactionRepositoryPort {
    void save(UUID uuid, Transaction transaction);
}
