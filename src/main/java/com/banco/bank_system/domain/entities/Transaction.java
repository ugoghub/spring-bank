package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InvalidTransactionException;
import com.banco.bank_system.domain.valueobject.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public class Transaction {
    private final TransactionId id;
    private final OperationId operationId;
    private final AccountId accountId;
    private final TransactionType type;
    private final Money amount;
    private final AccountIdentity sourceIdentity;
    private final AccountIdentity destinationIdentity;
    private final LocalDateTime dateTime;

    private Transaction(
            TransactionId id,
            OperationId operationId,
            AccountId accountId,
            TransactionType type,
            Money amount,
            AccountIdentity sourceIdentity,
            AccountIdentity destinationIdentity,
            LocalDateTime dateTime
    ) {

        if(id == null){
            throw new InvalidTransactionException("ID inválido");
        }

        validateTransactionState(accountId, type, operationId, sourceIdentity, destinationIdentity, dateTime);

        validateAmount(amount);

        this.id = id;
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceIdentity = sourceIdentity;
        this.destinationIdentity = destinationIdentity;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
    }

    private Transaction(OperationId operationId,
                        AccountId accountId,
                        TransactionType type,
                        Money amount,
                        AccountIdentity sourceIdentity,
                        AccountIdentity destinationIdentity,
                        Clock clock) {

        if(clock == null) throw new InvalidTransactionException("Clock inválido");


        LocalDateTime now = LocalDateTime.now(clock);

        validateTransactionState(accountId, type, operationId, sourceIdentity, destinationIdentity, now);

        validateAmount(amount);

        this.id = TransactionId.generate();
        this.operationId = operationId;
        this.accountId = accountId;
        this.sourceIdentity = sourceIdentity;
        this.destinationIdentity = destinationIdentity;
        this.type = type;
        this.amount = amount;
        this.dateTime = now;
    }

    public static Transaction restore(
            TransactionId id,
            OperationId operationId,
            AccountId accountId,
            TransactionType type,
            Money amount,
            AccountIdentity sourceIdentity,
            AccountIdentity destinationIdentity,
            LocalDateTime dateTime
    ) {
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

    public static Transaction deposit(AccountId accountId,
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

    public static Transaction withdraw(AccountId accountId,
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

    public static Transaction transferSent(AccountId accountId,
                                           OperationId operationId,
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

    public static Transaction transferReceived(AccountId accountId,
                                               OperationId operationId,
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

    public static Transaction interest(AccountId accountId,
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

    public TransactionId getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public OperationId getOperationId() {
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

    private static void validateTransactionState(
            AccountId accountId,
            TransactionType type,
            OperationId operationId,
            AccountIdentity sourceIdentity,
            AccountIdentity destinationIdentity,
            LocalDateTime dateTime
    ) {
        //validação defensiva

        if(accountId == null){
            throw new InvalidTransactionException("ID da conta inválido");
        }

        if(dateTime == null){
            throw new InvalidTransactionException("Horário inválido");
        }

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

    private static void validateTransfer(OperationId operationId,
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
