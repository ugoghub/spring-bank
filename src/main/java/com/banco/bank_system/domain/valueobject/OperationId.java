package com.banco.bank_system.domain.valueobject;

import java.util.UUID;

public record OperationId(UUID id) {

    public static OperationId generate(){
        return new OperationId(UUID.randomUUID());
    }
}
