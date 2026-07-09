package com.banco.bank_system.presentation.dto.request.client;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.processing.Pattern;


public record CreateClientRequest(

        @Schema(
                description = "Nome completo do cliente",
                example = "Marcy Mendes"
        )
        @NotBlank
        String name,

        @Schema(
                description = "CPF do cliente",
                example = "52998224725"
        )
        @Pattern()
        String cpf,

        @Schema(
                description = "E-mail do cliente",
                example = "marcy.mendes@gmail.com"
        )
        String email
) {}
