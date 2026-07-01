package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransferOutput;

import java.time.format.DateTimeFormatter;

public record TransferResponse(
        String operationId,
        String source,
        String destination,
        String amount,
        String transactionDate
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static TransferResponse from(TransferOutput output){
        return new TransferResponse(
                output.operationId().toString(),
                output.source().toString(),
                output.destination().toString(),
                output.amount().value().toString(),
                output.transactionDate().format(formatter)
        );
    }
}
