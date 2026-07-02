package com.banco.bank_system.domain.valueobject;

import java.util.UUID;

public record TransactionId(UUID id) {

    public static TransactionId generate(){
        return new TransactionId(UUID.randomUUID());
    }

}
