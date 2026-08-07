package com.banco.bank_system.presentation.dto.request.account;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateAccountRequest(

        @Schema(
                description = "CPF do titular da conta",
                example = "52998224725"
        )
        @NotBlank
        String cpf,

        @Schema(
                description = "Tipo da conta",
                allowableValues = {"CHECKING", "SAVINGS"},
                example = "CHECKING"
        )
        @NotBlank
        String accountType
) {}
