package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransferOutput;

import java.time.format.DateTimeFormatter;

public record TransferResponse(
        String operationId,
        String sourceBranch,
        String sourceAccountNumber,
        String destinationBranch,
        String destinationAccountNumber,
        String amount,
        String transactionDate
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static TransferResponse from(TransferOutput output){
        return new TransferResponse(
                output.operationId().toString(),
                output.source().branch(),
                output.source().accountNumber(),
                output.destination().branch(),
                output.destination().accountNumber(),
                output.amount().value().toString(),
                output.transactionDate().format(formatter)
        );
    }
}
