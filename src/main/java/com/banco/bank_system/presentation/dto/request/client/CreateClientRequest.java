package com.banco.bank_system.presentation.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record CreateClientRequest(

        @Schema(
                description = "Nome completo do cliente",
                example = "Bruno Fernandes"
        )
        @NotBlank
        String name,

        @Schema(
                description = "CPF do cliente",
                example = "52998224725"
        )
        @NotBlank
        String cpf,

        @Schema(
                description = "E-mail do cliente",
                example = "brunofernandes@email.com"
        )
        @NotBlank
        String email
) {}
