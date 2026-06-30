package com.banco.bank_system.presentation.dto.response;

import com.banco.bank_system.application.client.dto.output.ChangeClientEmailOutput;

public record ChangeEmailResponse(String name){

    public static ChangeEmailResponse from(ChangeClientEmailOutput output) {
        return new ChangeEmailResponse(
                output.name().value()
        );
    }
}
