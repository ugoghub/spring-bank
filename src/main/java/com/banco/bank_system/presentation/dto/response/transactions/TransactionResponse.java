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
        UUID source,
        UUID destination,
        String dateTime
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static List<TransactionResponse> from(List<TransactionDTO> output) {
        return output.stream()
                .map(t -> new TransactionResponse(
                                t.id(),
                                t.operationId(),
                                t.type().toString(),
                                t.amount().value().toString(),
                                t.sourceId(),
                                t.destinationId(),
                                formatter.format(t.dateTime())
                        )
                )
                .toList();
    }
}
