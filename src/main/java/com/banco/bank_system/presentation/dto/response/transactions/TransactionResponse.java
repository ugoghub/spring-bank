package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransactionDTO;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
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

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    public static List<TransactionResponse> from(List<TransactionDTO> output) {
        return output.stream()
                .map(t -> new TransactionResponse(
                                t.id().id(),

                                t.operationId() == null
                                        ? null
                                        : t.operationId().id(),

                                t.type().toString(),
                                FORMAT.format(t.amount().value()),
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
