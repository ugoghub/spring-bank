package com.banco.bank_system.infrastructure.database.entities;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
@NoArgsConstructor
public class AccountEntity {

    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    // AccountIdentity (Value Object) desmembrado em colunas na tabela
    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "agency_number", nullable = false)
    private String branch;

    @Column(nullable = false)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type", nullable = false)
    private AccountType accountType;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public AccountEntity(UUID id, UUID clientId, String accountNumber, String branch,
                         BigDecimal balance, AccountType accountType, LocalDateTime createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.accountNumber = accountNumber;
        this.branch = branch;
        this.balance = balance;
        this.accountType = accountType;
        this.createdAt = createdAt;
    }

    public Account toDomain(Clock clock) {
        AccountIdentity identity = new AccountIdentity(this.accountNumber, this.branch);
        Money currentBalance = Money.of(this.balance);

        if (this.accountType == AccountType.CHECKING) {
            return new CheckingAccount(id, new AccountIdentity(branch, accountNumber), clock);
        } else {
            return new SavingsAccount(id, new AccountIdentity(branch, accountNumber), clock);
        }
    }

    public static AccountEntity fromDomain(Account account) {
        AccountType type = (account instanceof CheckingAccount) ? AccountType.CHECKING : AccountType.SAVINGS;

        return new AccountEntity(
                account.getId(),
                account.getClientId(),
                account.getAccountIdentity().accountNumber(),  // Extrai do Value Object
                account.getAccountIdentity().branch(),  // Extrai do Value Object
                account.getBalance().value(),           // Extrai do Value Object Money (BigDecimal)
                type,
                account.getCreationTime()
        );
    }
}