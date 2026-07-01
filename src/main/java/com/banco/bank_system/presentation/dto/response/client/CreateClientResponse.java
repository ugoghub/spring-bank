package com.banco.bank_system.presentation.dto.response.client;

import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.domain.valueobject.ClientId;

import java.util.UUID;

public record CreateClientResponse(
        UUID id,
        String name,
        String cpf,
        String email
) {

    public static CreateClientResponse from(CreateClientOutput output) {
        return new CreateClientResponse(
                output.id().id(),
                output.name().value(),
                output.cpf().value(),
                output.email().value()
        );
    }
}
