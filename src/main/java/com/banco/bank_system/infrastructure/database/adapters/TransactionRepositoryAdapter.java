package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import com.banco.bank_system.infrastructure.database.sql.SpringDataTransactionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class TransactionRepositoryAdapter implements TransactionRepositoryPort {

    private final SpringDataTransactionRepository repository;

    public TransactionRepositoryAdapter(SpringDataTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Transaction transaction) {

        TransactionEntity transactionEntity = TransactionEntity.fromDomain(transaction);

        repository.save(transactionEntity);
    }

    @Override
    public List<Transaction> findByAccountId(UUID clientId) {
        return repository.findByAccountId(clientId)
                .stream()
                .map(TransactionEntity::toDomain)
                .toList();
    }
}
