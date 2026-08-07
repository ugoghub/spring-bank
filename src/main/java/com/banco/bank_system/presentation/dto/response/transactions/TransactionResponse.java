package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.banco.bank_system.presentation.util.DateFormatter;

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

    public static TransactionResponse from(TransactionDTO output) {
        return new TransactionResponse(
                output.id().id(),
                output.operationId() == null ? null : output.operationId().id(),
                output.type().toString(),
                CurrencyFormatter.format(output.amount()),
                output.source_branch(),
                output.source_accountNumber(),
                output.destination_branch(),
                output.destination_accountNumber(),
                DateFormatter.format(output.dateTime())
        );
    }
}
