package com.banco.bank_system.presentation.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ChangeClientNameRequest(

        @Schema(
                description = "Novo nome do cliente",
                example = "Marcy Mendes"
        )
        @NotBlank
        String name
) {}
