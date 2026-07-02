package com.banco.bank_system.domain.valueobject;

import java.util.UUID;

public record ClientId(UUID id){

    public static ClientId generate(){
        return new ClientId(UUID.randomUUID());
    }
}
