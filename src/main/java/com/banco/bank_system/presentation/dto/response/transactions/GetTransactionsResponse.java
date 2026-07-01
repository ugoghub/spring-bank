package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.GetTransactionsOutput;

import java.time.format.DateTimeFormatter;
import java.util.List;

public record GetTransactionsResponse(List<TransactionResponse> transactions) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static List<TransactionResponse> from(GetTransactionsOutput output) {
        return output.transactions().stream()
                .map(t -> new TransactionResponse(
                                t.id(),
                                t.operationId(),
                                t.type().toString(),
                                t.amount().value().toString(),
                                t.source(),
                                t.destination(),
                                formatter.format(t.dateTime())
                        )
                )
                .toList();
    }

}
