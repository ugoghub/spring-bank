package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;

import java.time.format.DateTimeFormatter;
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

    public static WithdrawResponse from(WithdrawOutput output){
        return new WithdrawResponse(
                output.accountId().id(),
                CurrencyFormatter.format(output.withdrawnAmount()),
                CurrencyFormatter.format(output.newBalance()),
                output.transactionId().id(),
                output.transactionDate().format(formatter)
        );
    }
}
