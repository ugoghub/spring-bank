package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;

import java.util.List;

public record GetClientAccountsResponse(
        List<AccountResponse> accounts
) {

    public static GetClientAccountsResponse from(GetClientAccountsOutput output) {

        return new GetClientAccountsResponse(
                output.accountIdentities()
                        .stream()
                        .map(AccountResponse::from)
                        .toList()
        );
    }
}
