package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransactionDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static List<TransactionResponse> from(List<TransactionDTO> output) {
        return output.stream()
                .map(t -> new TransactionResponse(
                                t.id().id(),
                                t.operationId().id(),
                                t.type().toString(),
                                t.amount().value().toString(),
                                t.source_branch(),
                                t.source_accountNumber(),
                                t.destination_branch(),
                                t.destination_accountNumber(),
                                formatter.format(t.dateTime())
                        )
                )
                .toList();
    }
}
