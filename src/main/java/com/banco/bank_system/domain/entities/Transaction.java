package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InvalidTransactionException;
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
    private final UUID sourceId;
    private final UUID destinationId;
    private final LocalDateTime dateTime;

    private Transaction(
            UUID id,
            UUID operationId,
            UUID accountId,
            TransactionType type,
            Money amount,
            UUID sourceId,
            UUID destinationId,
            LocalDateTime dateTime
    ) {
        this.id = id;
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    private Transaction(UUID operationId,
                        UUID accountId,
                        TransactionType type,
                        Money amount,
                        UUID sourceId,
                        UUID destinationId,
                        Clock clock) {

        validateTransactionState(operationId, type, sourceId, destinationId);

        validateAmount(amount);

        if(clock == null) {
            throw new IllegalArgumentException("Horário inválido");
        }

        this.id = UUID.randomUUID();
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
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
            UUID sourceId,
            UUID destinationId,
            LocalDateTime dateTime
    ) {
        validateTransactionState(operationId, type, sourceId, destinationId);

        validateAmount(amount);

        return new Transaction(
                id,
                operationId,
                accountId,
                type,
                amount,
                sourceId,
                destinationId,
                dateTime
        );
    }


    // =========================
    // Factory Methods
    // =========================

    public static Transaction deposit(UUID accountId,
                                      Money amount,
                                      Clock clock) {
        return new Transaction(null, accountId, TransactionType.DEPOSIT, amount, null, accountId, clock);
    }

    public static Transaction withdraw(UUID accountId,
                                       Money amount,
                                       Clock clock) {
        return new Transaction(null, accountId, TransactionType.WITHDRAW, amount, accountId, null, clock);
    }

    public static Transaction transferSent(UUID operationId,
                                           UUID from,
                                           UUID to,
                                           Money amount,
                                           Clock clock) {
        return new Transaction(operationId, from, TransactionType.TRANSFER_SENT, amount, from, to, clock);
    }

    public static Transaction transferReceived(UUID operationId,
                                               UUID from,
                                               UUID to,
                                               Money amount,
                                               Clock clock) {
        return new Transaction(operationId, to, TransactionType.TRANSFER_RECEIVED, amount, from, to, clock);
    }

    public static Transaction interest(UUID accountId,
                                       Money amount,
                                       Clock clock) {
        return new Transaction(null, accountId, TransactionType.INTEREST, amount, null, accountId, clock);
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

    public UUID getSource() {
        return sourceId;
    }

    public UUID getDestination() {
        return destinationId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    // =========================
    // Validation
    // =========================

    private static void validateTransactionState(UUID operationId,
                                                 TransactionType type,
                                                 UUID sourceId,
                                                 UUID destinationId) {
        //validação defensiva

        if(type == null){
            throw new InvalidTransactionException(
                    "Tipo de transação inválido"
            );
        }

        switch (type) {

            case DEPOSIT -> validateDeposit(sourceId, destinationId);

            case WITHDRAW -> validateWithdraw(sourceId, destinationId);

            case TRANSFER_SENT, TRANSFER_RECEIVED -> validateTransfer(operationId, sourceId, destinationId);

            case INTEREST -> validateInterest(sourceId, destinationId);
        }
    }

    private static void validateDeposit(UUID sourceId,
                                        UUID destinationId) {
        if (sourceId != null || destinationId == null) {
            throw new InvalidTransactionException("DEPÓSITO não deve possuir conta de origem");
        }
    }

    private static void validateWithdraw(UUID sourceId,
                                         UUID destinationId) {
        if (sourceId == null || destinationId != null) {
            throw new InvalidTransactionException("SAQUE não deve possuir conta de destino");
        }
    }

    private static void validateTransfer(UUID operationId,
                                         UUID sourceId,
                                         UUID destinationId) {
        if (operationId == null){
            throw new InvalidTransactionException("Toda transferência deve possuir um ID de operação");
        }
        if (sourceId == null || destinationId == null) {
            throw new InvalidTransactionException("Transferência não deve possuir origem e/ou destino nulls");
        }
    }

    private static void validateInterest(UUID sourceId,
                                         UUID destinationId) {

        if (sourceId != null || destinationId == null) {
            throw new InvalidTransactionException(
                    "RENDIMENTO deve possuir apenas conta destino"
            );
        }
    }

    private static void validateAmount(Money amount) {

        if(amount == null){
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
