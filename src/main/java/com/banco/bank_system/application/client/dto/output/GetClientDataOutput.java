package com.banco.bank_system.application.client.dto.output;

public record GetClientDataOutput(
        String name,
        String cpf,
        String email
) {
}
