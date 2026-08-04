package com.banco.bank_system.presentation.dto.response.account;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.presentation.util.CurrencyFormatter;

import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record CreateAccountResponse(
        UUID id,
        UUID clientId,
        String branch,
        String accountNumber,
        String createdAt,
        String balance
) {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static CreateAccountResponse from(CreateAccountOutput output) {
        return new CreateAccountResponse(
                output.id().id(),
                output.clientId().id(),
                output.accountIdentity().branch(),
                output.accountIdentity().accountNumber(),
                output.creationTime().format(formatter),
                CurrencyFormatter.format(output.balance())
        );
    }
}
