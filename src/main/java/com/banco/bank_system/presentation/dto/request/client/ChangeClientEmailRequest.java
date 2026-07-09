package com.banco.bank_system.presentation.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record ChangeClientEmailRequest(

        @Schema(
                description = "Novo e-mail do cliente",
                example = "marcy.mendes@gmail.com"
        )
        @Email
        String email
) {}
