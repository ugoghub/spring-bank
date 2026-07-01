package com.banco.bank_system.application.transaction.dto;

import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record TransactionDTO(
        UUID id,
        UUID operationId,
        TransactionType type,
        Money amount,
        UUID sourceId,
        UUID destinationId,
        LocalDateTime dateTime
) {

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
