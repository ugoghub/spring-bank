package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;

import java.time.format.DateTimeFormatter;

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

    public static TransferResponse from(TransferOutput output){
        return new TransferResponse(
                output.operationId().id().toString(),
                output.source().branch(),
                output.source().accountNumber(),
                output.destination().branch(),
                output.destination().accountNumber(),
                CurrencyFormatter.format(output.amount()),
                output.transactionDate().format(formatter)
        );
    }
}
