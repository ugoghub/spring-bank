package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.WithdrawOutput;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

public record WithdrawResponse(
        UUID id,
        String withdrawnAmount,
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

    public static WithdrawResponse from(WithdrawOutput output){
        return new WithdrawResponse(
                output.accountId().id(),
                FORMAT.format(output.withdrawnAmount().value()),
                FORMAT.format(output.newBalance().value()),
                output.transactionId().id(),
                output.transactionDate().format(formatter)
        );
    }
}
