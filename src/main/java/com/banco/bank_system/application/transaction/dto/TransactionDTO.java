package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.domain.valueobject.OperationId;
import com.banco.bank_system.domain.valueobject.TransactionId;

import java.time.LocalDateTime;
import java.util.List;
public record TransactionDTO(
        TransactionId id,
        OperationId operationId,
        TransactionType type,
        Money amount,
        String source_branch,
        String source_accountNumber,
        String destination_branch,
        String destination_accountNumber,
        LocalDateTime dateTime
) {

    public static TransactionDTO from(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getOperationId(),
                transaction.getType(),
                transaction.getAmount(),
                branch(transaction.getSource()),
                accountNumber(transaction.getSource()),
                branch(transaction.getDestination()),
                accountNumber(transaction.getDestination()),
                transaction.getDateTime()
        );
    }

    private static String branch(AccountIdentity accountIdentity) {
        return accountIdentity == null ? null : accountIdentity.branch();
    }

    private static String accountNumber(AccountIdentity accountIdentity) {
        return accountIdentity == null ? null : accountIdentity.accountNumber();
    }
}