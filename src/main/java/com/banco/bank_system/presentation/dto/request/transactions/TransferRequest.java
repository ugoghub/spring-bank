package com.banco.bank_system.presentation.dto.request.transactions;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TransferRequest(

        @Schema(
                description = "Agência da conta de origem",
                example = "01"
        )
        @NotBlank
        String fromBranch,

        @Schema(
                description = "Número da conta de origem",
                example = "123456-1"
        )
        @NotBlank
        String fromAccountNumber,

        @Schema(
                description = "Agência da conta de destino",
                example = "02"
        )
        @NotBlank
        String toBranch,

        @Schema(
                description = "Número da conta de destino",
                example = "654321-1"
        )
        @NotBlank
        String toAccountNumber,

        @Schema(
                description = "Valor da transferência",
                example = "100.00"
        )
        @NotBlank
        String amount
) {}
