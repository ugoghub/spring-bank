package com.banco.bank_system.infrastructure.database.mapper;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.domain.valueobject.OperationId;
import com.banco.bank_system.domain.valueobject.TransactionId;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.infrastructure.database.entities.TransactionEntity;

import java.util.UUID;

public final class TransactionMapper {

    private TransactionMapper() {
    }

    public static TransactionEntity fromDomain(Transaction transaction) {

        return new TransactionEntity(
                transaction.getId().id(),
                operationId(transaction.getOperationId()),
                transaction.getAccountId().id(),
                transaction.getType(),
                transaction.getAmount().value(),
                branch(transaction.getSource()),
                accountNumber(transaction.getSource()),
                branch(transaction.getDestination()),
                accountNumber(transaction.getDestination()),
                transaction.getDateTime()
        );
    }

    public static Transaction toDomain(TransactionEntity entity) {

        return Transaction.restore(
                new TransactionId(entity.getId()),
                operationId(entity.getOperationId()),
                new AccountId(entity.getAccountId()),
                entity.getType(),
                Money.of(entity.getAmount()),
                accountIdentity(
                        entity.getSourceBranch(),
                        entity.getSourceAccountNumber()
                ),
                accountIdentity(
                        entity.getDestinationBranch(),
                        entity.getDestinationAccountNumber()
                ),
                entity.getCreatedAt()
        );
    }

    private static UUID operationId(OperationId id) {
        return id == null ? null : id.id();
    }

    private static OperationId operationId(UUID id) {
        return id == null ? null : new OperationId(id);
    }

    private static AccountIdentity accountIdentity(
            String branch,
            String accountNumber
    ) {
        return branch == null ? null : new AccountIdentity(branch, accountNumber);
    }

    private static String branch(AccountIdentity identity) {
        return identity == null ? null : identity.branch();
    }

    private static String accountNumber(AccountIdentity identity) {
        return identity == null ? null : identity.accountNumber();
    }
}