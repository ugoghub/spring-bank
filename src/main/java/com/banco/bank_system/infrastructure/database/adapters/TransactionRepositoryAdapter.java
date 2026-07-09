package com.banco.bank_system.infrastructure.database.adapters;

import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public Page<Transaction> findByAccountId(
            AccountId accountId,
            Pageable pageable
    ) {
        return repository
                .findByAccountId(accountId.id(), pageable)
                .map(TransactionEntity::toDomain);
    }
}
