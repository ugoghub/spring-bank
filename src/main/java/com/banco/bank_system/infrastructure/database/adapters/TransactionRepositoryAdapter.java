package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final JpaTransactionRepository repository;

    public TransactionRepositoryAdapter(JpaTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Transaction transaction) {

        TransactionEntity entity =
                TransactionEntity.fromDomain(transaction);

        repository.save(entity);
    }

    @Override
    public List<Transaction> findByAccountId(UUID accountId) {
        return repository.findByAccountId(accountId)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
