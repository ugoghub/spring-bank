package com.banco.bank_system.domain.entities;

import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.exception.*;
import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Account {

    private final AccountId id;
    private final ClientId clientId;
    private final AccountIdentity accountIdentity;
    private final LocalDateTime creationTime;
    private Money balance;

    protected Account(
            AccountId id,
            ClientId clientId,
            AccountIdentity accountIdentity,
            Money balance,
            LocalDateTime creationTime
    ) {

        if(id == null){
            throw new InvalidAccountIdException("ID inválido");
        }

        if(clientId == null){
            throw new InvalidClientIdException("Cliente inválido");
        }

        if(accountIdentity == null){
            throw new InvalidAccountIdentityException("identidade da conta inválida");
        }

        if(balance == null){
            throw new InvalidMoneyException("Saldo inválido");
        }

        if(creationTime == null){
            throw new InvalidClockException("Data inválida");
        }


        this.id = id;
        this.clientId = clientId;
        this.accountIdentity = accountIdentity;
        this.balance = balance;
        this.creationTime = creationTime;
    }

    // =========================
    // Actions
    // =========================

    public void deposit(Money amount) {

        validatePositiveAmount(amount);

        increaseBalance(amount);
    }

    public void withdraw(Money amount) {

        validatePositiveAmount(amount);

        decreaseBalance(amount);
    }

    protected final void increaseBalance(Money amount) {
        balance = balance.add(amount);
    }

    protected final void decreaseBalance(Money amount) {

        Money newBalance = balance.subtract(amount);

        if (newBalance.compareTo(minimumAllowedBalance()) < 0) {
            throw new InsufficientBalanceException(
                    "Saldo insuficiente"
            );
        }

        balance = newBalance;
    }

    private void validatePositiveAmount(Money amount) {

        if(amount == null){
            throw new InvalidMoneyException(
                    "Valor não pode ser null"
            );
        }

        if (amount.isNegativeOrZero()) {
            throw new InvalidMoneyException(
                    "Valor deve ser maior que zero"
            );
        }
    }

    protected abstract Money minimumAllowedBalance();

    public boolean isRemovable() {
        return balance.isZero();
    }

    public void validateRemoval() {
        if (!balance.isZero()) {
            throw new CannotRemoveAccountException(
                    "Conta não pode ser excluída com saldo diferente de zero"
            );
        }
    }

    // =========================
    // Getters
    // =========================

    public AccountId getId() {
        return id;
    }

    public ClientId getClientId() {
        return clientId;
    }

    public AccountIdentity getAccountIdentity() {
        return accountIdentity;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Money getBalance() {
        return balance;
    }

    public abstract AccountType getType();

    // =========================
    // Equals e Hashcode
    // =========================

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Account account)) return false;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
