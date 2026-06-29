package com.banco.bank_system.application.client.dto.output;

import java.util.UUID;

public record CreateClientOutput(
        UUID id,
        String name,
        String cpf,
        String email
) {}
