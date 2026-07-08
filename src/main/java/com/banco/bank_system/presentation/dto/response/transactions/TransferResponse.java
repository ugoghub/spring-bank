package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransferOutput;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public record TransferResponse(
        String operationId,
        String source_branch,
        String source_accountNumber,
        String destination_branch,
        String destination_accountNumber,
        String amount,
        String transactionDate
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    public static TransferResponse from(TransferOutput output){
        return new TransferResponse(
                output.operationId().id().toString(),
                output.source().branch(),
                output.source().accountNumber(),
                output.destination().branch(),
                output.destination().accountNumber(),
                FORMAT.format(output.amount().value()),
                output.transactionDate().format(formatter)
        );
    }
}
