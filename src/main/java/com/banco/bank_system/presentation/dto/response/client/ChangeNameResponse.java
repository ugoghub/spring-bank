package com.banco.bank_system.presentation.dto.response.client;

import com.banco.bank_system.application.client.dto.output.ChangeClientNameOutput;

public record ChangeNameResponse(String name){

    public static ChangeNameResponse from(ChangeClientNameOutput output) {
        return new ChangeNameResponse(
                output.name().value()
        );
    }
}
