package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.DepositOutput;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    public static DepositResponse from(DepositOutput output){
        return new DepositResponse(
                output.accountId().id(),
                FORMAT.format(output.depositedAmount().value()),
                output.newBalance().value().toString(),
                output.transactionId().id(),
                output.transactionDate().format(formatter)
        );
    }
}
