package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;

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
                output.id(),
                output.clientId(),
                output.accountIdentity().branch(),
                output.accountIdentity().accountNumber(),
                output.creationTime().format(formatter),
                output.balance().toString()
        );
    }
}
