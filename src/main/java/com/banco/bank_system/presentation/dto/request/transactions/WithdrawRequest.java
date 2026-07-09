package com.banco.bank_system.presentation.dto.request.transactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record WithdrawRequest(

        @Schema(
                description = "Agência da conta",
                example = "01"
        )
        @NotBlank
        String branch,

        @Schema(
                description = "Número da conta",
                example = "123456-1"
        )
        @NotBlank
        String accountNumber,

        @Schema(
                description = "Valor do saque",
                example = "100.00"
        )
        @NotBlank
        String amount
) {}