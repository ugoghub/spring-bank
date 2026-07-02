package com.banco.bank_system.domain.valueobject;

import java.util.UUID;

public record ClientId(UUID id){

    public static ClientId generete(){
        return new ClientId(UUID.randomUUID());
    }
}
