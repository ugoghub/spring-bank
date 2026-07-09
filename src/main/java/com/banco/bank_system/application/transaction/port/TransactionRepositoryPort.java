package com.banco.bank_system.application.transaction.port;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionRepositoryPort {
    void save(Transaction transaction);

    Page<Transaction> findByAccountId(
            AccountId accountId,
            Pageable pageable
    );
}
