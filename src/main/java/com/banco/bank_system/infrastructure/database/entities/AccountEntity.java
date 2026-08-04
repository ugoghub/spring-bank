package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.enums.AccountType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tb_accounts",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "account_number",
                                "agency_number"
                        }
                )
        }
)

@Getter
@NoArgsConstructor
public class AccountEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID id;


    @Column(
            name = "client_id",
            nullable = false,
            updatable = false
    )
    private UUID clientId;


    @Column(
            name = "account_number",
            nullable = false,
            updatable = false
    )
    private String accountNumber;


    @Column(
            name = "agency_number",
            nullable = false,
            updatable = false
    )
    private String branch;


    @Column(nullable = false)
    private BigDecimal balance;


    @Enumerated(EnumType.STRING)
    @Column(
            name = "account_type",
            nullable = false
    )
    private AccountType accountType;


    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(name = "last_interest_applied_at")
    private LocalDateTime lastInterestAppliedAt;

    public AccountEntity(
            UUID id,
            UUID clientId,
            String accountNumber,
            String branch,
            BigDecimal balance,
            AccountType accountType,
            LocalDateTime createdAt,
            LocalDateTime lastInterestAppliedAt
    ) {

        this.id = id;
        this.clientId = clientId;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
        this.lastInterestAppliedAt = lastInterestAppliedAt;
    }
}