package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_transactions")

@Getter
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "operation_id")
    private UUID operationId;

    @Column(name = "accountId")
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "source_branch")
    private String source_branch;

    @Column(name = "source_accountNumber")
    private String source_accountNumber;

    @Column(name = "destination_branch")
    private String destination_branch;

    @Column(name = "destination_accountNumber")
    private String destination_accountNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity(
            UUID id,
            UUID operationId,
            UUID accountId,
            TransactionType type,
            BigDecimal amount,
            String source_branch,
            String source_accountNumber,
            String destination_branch,
            String destination_accountNumber,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.operationId = operationId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.source_branch = source_branch;
        this.source_accountNumber = source_accountNumber;
        this.destination_branch = destination_branch;
        this.destination_accountNumber = destination_accountNumber;
        this.createdAt = createdAt;
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

    public Transaction toDomain() {

        return Transaction.restore(
                new TransactionId(id),
                operationId == null ? null : new OperationId(operationId),
                new AccountId(accountId),
                type,
                Money.of(amount),
                accountIdentity(source_branch, source_accountNumber),
                accountIdentity(destination_branch, destination_accountNumber),
                createdAt
        );
    }

    private static UUID operationId(OperationId id) {
        return id == null ? null : id.id();
    }

    private static AccountIdentity accountIdentity(String branch, String account) {
        return branch == null ? null : new AccountIdentity(branch, account);
    }

    private static String branch(AccountIdentity identity) {
        return identity == null ? null : identity.branch();
    }

    private static String accountNumber(AccountIdentity identity) {
        return identity == null ? null : identity.accountNumber();
    }
}
