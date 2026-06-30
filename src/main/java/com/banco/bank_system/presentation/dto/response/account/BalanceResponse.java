package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;

public record BalanceResponse(String balance) {

    public static BalanceResponse from(GetBalanceOutput output) {
        return new BalanceResponse(
                output.balance().value().toString()
        );
    }
}
