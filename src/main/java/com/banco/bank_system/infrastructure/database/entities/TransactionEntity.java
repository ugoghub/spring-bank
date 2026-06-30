package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "source_account_number")
    private String sourceAccountNumber;

    @Column(name = "source_branch")
    private String sourceBranch;

    @Column(name = "destination_account_number")
    private String destinationAccountNumber;

    @Column(name = "destination_branch")
    private String destinationBranch;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity(
            UUID id,
            UUID operationId,
            TransactionType type,
            BigDecimal amount,
            String sourceAccountNumber,
            String sourceBranch,
            String destinationAccountNumber,
            String destinationBranch,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.operationId = operationId;
        this.type = type;
        this.amount = amount;
        this.sourceAccountNumber = sourceAccountNumber;
        this.sourceBranch = sourceBranch;
        this.destinationAccountNumber = destinationAccountNumber;
        this.destinationBranch = destinationBranch;
        this.createdAt = createdAt;
    }

    public static TransactionEntity fromDomain(Transaction transaction) {

        return new TransactionEntity(
                transaction.getId(),
                transaction.getOperationId(),
                transaction.getType(),
                transaction.getAmount().value(),

                transaction.getSourceIdentity() != null
                        ? transaction.getSourceIdentity().accountNumber()
                        : null,

                transaction.getSourceIdentity() != null
                        ? transaction.getSourceIdentity().branch()
                        : null,

                transaction.getDestinationIdentity() != null
                        ? transaction.getDestinationIdentity().accountNumber()
                        : null,

                transaction.getDestinationIdentity() != null
                        ? transaction.getDestinationIdentity().branch()
                        : null,

                transaction.getDateTime()
        );
    }

    public Transaction toDomain() {

        AccountIdentity source =
                sourceAccountNumber == null
                        ? null
                        : new AccountIdentity(sourceBranch, sourceAccountNumber);

        AccountIdentity destination =
                destinationAccountNumber == null
                        ? null
                        : new AccountIdentity(destinationBranch, destinationAccountNumber);

        return Transaction.restore(
                id,
                operationId,
                type,
                Money.of(amount),
                source,
                destination,
                createdAt
        );
    }
}
