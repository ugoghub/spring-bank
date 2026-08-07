package com.banco.bank_system.presentation.dto.response.transactions;

import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.banco.bank_system.presentation.util.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record WithdrawResponse(
        UUID id,
        String withdrawnAmount,
        String newBalance,
        UUID transactionId,
        String transactionDate
) {
    public static WithdrawResponse from(WithdrawOutput output){
        return new WithdrawResponse(
                output.accountId().id(),
                CurrencyFormatter.format(output.withdrawnAmount()),
                CurrencyFormatter.format(output.newBalance()),
                output.transactionId().id(),
                DateFormatter.format(output.transactionDate())
        );
    }
}
