package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
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

    @Column(name = "accountId")
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "source")
    private UUID source;

    @Column(name = "destination")
    private UUID destination;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public TransactionEntity(
            UUID id,
            UUID operationId,
            UUID accountId,
            TransactionType type,
            BigDecimal amount,
            UUID source,
            UUID destination,
            LocalDateTime createdAt
    ) {
        this.id = id;
        this.operationId = operationId;
        this.accountId = accountId;
        this.type = type;
        this.amount = amount;
        this.source = source;
        this.destination = destination;
        this.createdAt = createdAt;
    }

    public static TransactionEntity fromDomain(Transaction transaction) {

        return new TransactionEntity(
                transaction.getId(),
                transaction.getOperationId(),
                transaction.getAccountId(),
                transaction.getType(),
                transaction.getAmount().value(),

                transaction.getSource() != null
                        ? transaction.getSource()
                        : null,

                transaction.getDestination() != null
                        ? transaction.getDestination()
                        : null,

                transaction.getDateTime()
        );
    }

    public Transaction toDomain() {

        return Transaction.restore(
                id,
                operationId,
                accountId,
                type,
                Money.of(amount),
                source,
                destination,
                createdAt
        );
    }
}
