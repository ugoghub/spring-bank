package com.banco.bank_system.domain.valueobject;

import java.util.UUID;

public record AccountId(UUID id) {

    public static AccountId generate(){
        return new AccountId(UUID.randomUUID());
    }
}
