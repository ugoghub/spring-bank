package com.banco.bank_system.presentation.dto.response.client;

import com.banco.bank_system.application.client.dto.output.GetClientDataOutput;

public record ClientDataResponse(
        String name,
        String cpf,
        String email
) {

    public static ClientDataResponse from(GetClientDataOutput output) {
        return new ClientDataResponse(
                output.name().value(),
                output.cpf().value(),
                output.email().value()
        );
    }
}
