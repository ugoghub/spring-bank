package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;
import com.banco.bank_system.presentation.util.DateFormatter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record GetClientAccountResponse(
        UUID id,
        UUID clientId,
        String branch,
        String accountNumber,
        String createdAt,
        String balance
) {

    public static GetClientAccountResponse from(GetClientAccountOutput output) {
        return new GetClientAccountResponse(
                output.id().id(),
                output.clientId().id(),
                output.accountIdentity().branch(),
                output.accountIdentity().accountNumber(),
                DateFormatter.format(output.creationTime()),
                CurrencyFormatter.format(output.balance())
        );
    }
}
