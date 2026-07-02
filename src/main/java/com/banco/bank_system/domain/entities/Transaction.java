package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InvalidTransactionException;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private final UUID id;
    private final UUID operationId;
    private final UUID accountId;
    private final TransactionType type;
    private final Money amount;
    private final AccountIdentity sourceIdentity;
    private final AccountIdentity destinationIdentity;
    private final LocalDateTime dateTime;

    private Transaction(
            UUID id,
            UUID operationId,
            UUID accountId,
            TransactionType type,
            Money amount,
            AccountIdentity sourceIdentity,
            AccountIdentity destinationIdentity,
            LocalDateTime dateTime
    ) {
        this.id = id;
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceIdentity = sourceIdentity;
        this.destinationIdentity = destinationIdentity;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    private Transaction(UUID operationId,
                        UUID accountId,
                        TransactionType type,
                        Money amount,
                        AccountIdentity sourceIdentity,
                        AccountIdentity destinationIdentity,
                        Clock clock) {

        validateTransactionState(operationId, type, sourceIdentity, destinationIdentity);

        validateAmount(amount);

        if (clock == null) {
            throw new IllegalArgumentException("Horário inválido");
        }

        this.id = UUID.randomUUID();
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceIdentity = sourceIdentity;
        this.destinationIdentity = destinationIdentity;
        this.type = type;
        this.amount = amount;
        this.dateTime = LocalDateTime.now(clock);
    }

    public static Transaction restore(
            UUID id,
            UUID operationId,
            UUID accountId,
            TransactionType type,
            Money amount,
            AccountIdentity sourceIdentity,
            AccountIdentity destinationIdentity,
            LocalDateTime dateTime
    ) {
        validateTransactionState(operationId, type, sourceIdentity, destinationIdentity);

        validateAmount(amount);


        return new Transaction(
                id,
                operationId,
                accountId,
                type,
                amount,
                sourceIdentity,
                destinationIdentity,
                dateTime
        );
    }


    // =========================
    // Factory Methods
    // =========================

    public static Transaction deposit(UUID accountId,
                                      AccountIdentity accountIdentity,
                                      Money amount,
                                      Clock clock) {
        return new Transaction(
                null,
                accountId,
                TransactionType.DEPOSIT,
                amount,
                null,
                accountIdentity,
                clock
        );
    }

    public static Transaction withdraw(UUID accountId,
                                       AccountIdentity accountIdentity,
                                       Money amount,
                                       Clock clock) {
        return new Transaction(null,
                accountId,
                TransactionType.WITHDRAW,
                amount,
                accountIdentity,
                null,
                clock
        );
    }

    public static Transaction transferSent(UUID operationId,
                                           UUID accountId,
                                           AccountIdentity from,
                                           AccountIdentity to,
                                           Money amount,
                                           Clock clock) {
        return new Transaction(operationId,
                accountId,
                TransactionType.TRANSFER_SENT,
                amount,
                from,
                to,
                clock
        );
    }

    public static Transaction transferReceived(UUID operationId,
                                               UUID accountId,
                                               AccountIdentity from,
                                               AccountIdentity to,
                                               Money amount,
                                               Clock clock) {
        return new Transaction(operationId,
                accountId,
                TransactionType.TRANSFER_RECEIVED,
                amount,
                from,
                to,
                clock
        );
    }

    public static Transaction interest(UUID accountId,
                                       AccountIdentity accountIdentity,
                                       Money amount,
                                       Clock clock) {
        return new Transaction(null,
                accountId,
                TransactionType.INTEREST,
                amount,
                null,
                accountIdentity,
                clock
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public UUID getOperationId() {
        return operationId;
    }

    public TransactionType getType() {
        return type;
    }

    public Money getAmount() {
        return amount;
    }

    public AccountIdentity getSource() {
        return sourceIdentity;
    }

    public AccountIdentity getDestination() {
        return destinationIdentity;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // =========================
    // Validation
    // =========================

    private static void validateTransactionState(UUID operationId,
                                                 TransactionType type,
                                                 AccountIdentity sourceIdentity,
                                                 AccountIdentity destinationIdentity) {
        //validação defensiva

        if (type == null) {
            throw new InvalidTransactionException(
                    "Tipo de transação inválido"
            );
        }

        switch (type) {

            case DEPOSIT -> validateDeposit(sourceIdentity, destinationIdentity);

            case WITHDRAW -> validateWithdraw(sourceIdentity, destinationIdentity);

            case TRANSFER_SENT, TRANSFER_RECEIVED -> validateTransfer(operationId, sourceIdentity, destinationIdentity);

            case INTEREST -> validateInterest(sourceIdentity, destinationIdentity);
        }
    }

    private static void validateDeposit(AccountIdentity sourceIdentity,
                                        AccountIdentity destinationIdentity) {
        if (sourceIdentity != null || destinationIdentity == null) {
            throw new InvalidTransactionException("Depósitos possuem apenas conta de destino.");
        }
    }

    private static void validateWithdraw(AccountIdentity sourceIdentity,
                                         AccountIdentity destinationIdentity) {
        if (sourceIdentity == null || destinationIdentity != null) {
            throw new InvalidTransactionException("Saques possuem apenas conta de origem.");
        }
    }

    private static void validateTransfer(UUID operationId,
                                         AccountIdentity sourceIdentity,
                                         AccountIdentity destinationIdentity) {
        if (operationId == null) {
            throw new InvalidTransactionException("Toda transferência deve possuir um ID de operação");
        }
        if (sourceIdentity == null || destinationIdentity == null) {
            throw new InvalidTransactionException("Transferência não deve possuir origem e/ou destino nulls");
        }
    }

    private static void validateInterest(AccountIdentity sourceIdentity,
                                         AccountIdentity destinationIdentity) {

        if (sourceIdentity != null || destinationIdentity == null) {
            throw new InvalidTransactionException(
                    "RENDIMENTO deve possuir apenas conta destino"
            );
        }
    }

    private static void validateAmount(Money amount) {

        if (amount == null) {
            throw new InvalidTransactionException("Valor não pode ser null");
        }

        if (amount.isNegativeOrZero()) {
            throw new InvalidTransactionException(
                    "Valor deve ser maior que zero"
            );
        }
    }

    // =========================
    // Equals e Hashcode
    // =========================

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
