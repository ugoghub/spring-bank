package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.banco.bank_system.presentation.util.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record DepositResponse(
        UUID id,
        String depositedAmount,
        String newBalance,
        UUID transactionId,
        String transactionDate
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static DepositResponse from(DepositOutput output){
        return new DepositResponse(
                output.accountId().id(),
                CurrencyFormatter.format(output.depositedAmount()),
                output.newBalance().value().toString(),
                output.transactionId().id(),
                DateFormatter.format(output.transactionDate())
        );
    }
}
