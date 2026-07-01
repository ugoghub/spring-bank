package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.entities.Transaction;

import java.util.List;

public record GetTransactionsOutput(List<TransactionDTO> transactions) {

    public static List<TransactionDTO> from(List<Transaction> transactions) {
        return transactions.stream()
                .map(t -> new TransactionDTO(
                                t.getId(),
                                t.getOperationId(),
                                t.getType(),
                                t.getAmount(),
                                t.getSource(),
                                t.getDestination(),
                                t.getDateTime()
                        )
                )
                .toList();
    }
}
