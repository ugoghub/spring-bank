package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.presentation.util.CurrencyFormatter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record TransactionResponse(
        UUID id,
        UUID operationId,
        String type,
        String amount,
        String source_branch,
        String source_accountNumber,
        String destination_branch,
        String destination_accountNumber,
        String dateTime
) {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static TransactionResponse from(TransactionDTO dto) {
        return new TransactionResponse(
                dto.id().id(),
                dto.operationId() == null ? null : dto.operationId().id(),
                dto.type().toString(),
                CurrencyFormatter.format(dto.amount()),
                dto.source_branch(),
                dto.source_accountNumber(),
                dto.destination_branch(),
                dto.destination_accountNumber(),
                FORMATTER.format(dto.dateTime())
        );
    }
}
