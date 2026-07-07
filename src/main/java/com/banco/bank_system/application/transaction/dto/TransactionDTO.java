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

    public static List<TransactionDTO> from(List<Transaction> transactions) {
        return transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getId(),
                        t.getOperationId(),
                        t.getType(),
                        t.getAmount(),

                        branch(t.getSource()),
                        accountNumber(t.getSource()),

                        branch(t.getDestination()),
                        accountNumber(t.getDestination()),

                        t.getDateTime()
                ))
                .toList();
    }

    private static String branch(AccountIdentity accountIdentity) {
        return accountIdentity == null
                ? null
                : accountIdentity.branch();
    }

    private static String accountNumber(AccountIdentity accountIdentity) {
        return accountIdentity == null
                ? null
                : accountIdentity.accountNumber();
    }
}
