package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.domain.valueobject.AccountIdentity;

public record AccountResponse(
        String branch,
        String accountNumber
) {

    public static AccountResponse from(AccountIdentity accountIdentity) {
        return new AccountResponse(
                accountIdentity.branch(),
                accountIdentity.accountNumber()
        );
    }
}
