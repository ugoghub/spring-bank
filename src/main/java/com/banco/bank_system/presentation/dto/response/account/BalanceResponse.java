package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;

public record BalanceResponse(String balance) {

    public static BalanceResponse from(GetBalanceOutput output) {
        return new BalanceResponse(
                CurrencyFormatter.format(output.balance())
        );
    }
}
