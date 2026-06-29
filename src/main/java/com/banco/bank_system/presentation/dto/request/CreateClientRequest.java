package com.banco.bank_system.presentation.dto.request;

public record CreateClientRequest(
        String name,
        String cpf,
        String email
) {
}
