package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.client.dto.output.GetClientDataOutput;

public record ClientData(
        String name,
        String cpf,
        String email
) {

    public static ClientData from(GetClientDataOutput output) {
        return new ClientData(
                output.name(),
                output.cpf(),
                output.email()
        );
    }
}
