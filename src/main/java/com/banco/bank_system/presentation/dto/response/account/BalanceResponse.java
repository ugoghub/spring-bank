package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;

import java.text.NumberFormat;
import java.util.Locale;

public record BalanceResponse(String balance) {

    private static final NumberFormat FORMAT =
            NumberFormat.getCurrencyInstance(
                    Locale.of("pt", "BR")
            );

    public static BalanceResponse from(GetBalanceOutput output) {
        return new BalanceResponse(
                FORMAT.format(output.balance().value())
        );
    }
}
