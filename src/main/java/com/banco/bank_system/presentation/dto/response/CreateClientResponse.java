package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.client.dto.output.CreateClientOutput;

import java.util.UUID;

public record CreateClientResponse(
        UUID id,
        String name,
        String cpf,
        String email
) {

    public static CreateClientResponse from(CreateClientOutput output) {
        return new CreateClientResponse(
                output.id(),
                output.name(),
                output.cpf(),
                output.email()
        );
    }
}
