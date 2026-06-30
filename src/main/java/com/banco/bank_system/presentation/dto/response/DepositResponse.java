package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.transaction.dto.DepositOutput;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record DepositResponse(
        String branch,
        String accountNumber,
        String depositedAmount,
        String newBalance,
        UUID transactionId,
        String transactionDate
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static DepositResponse from(DepositOutput output){
        return new DepositResponse(
                output.account().branch(),
                output.account().accountNumber(),
                output.depositedAmount().value().toString(),
                output.newBalance().value().toString(),
                output.transactionId(),
                output.transactionDate().format(formatter)
        );
    }
}
