package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.enums.TransactionType;
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

    @Column(name = "account_id")
    private UUID accountId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, updatable = false)
    private TransactionType type;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "source_branch")
    private String sourceBranch;

    @Column(name = "source_accountNumber")
    private String sourceAccountNumber;

    @Column(name = "destination_branch")
    private String destinationBranch;

    @Column(name = "destination_accountNumber")
    private String destinationAccountNumber;

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
        this.sourceBranch = source_branch;
        this.sourceAccountNumber = source_accountNumber;
        this.destinationBranch = destination_branch;
        this.destinationAccountNumber = destination_accountNumber;
        this.createdAt = createdAt;
    }
}
