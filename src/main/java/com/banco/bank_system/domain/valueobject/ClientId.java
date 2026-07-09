package com.banco.bank_system.domain.valueobject;

import com.banco.bank_system.domain.exception.InvalidClientIdException;

import java.util.UUID;

public record ClientId(UUID id){

    public ClientId{
        if(id == null) throw new InvalidClientIdException("ID do cliente inválido");
    }

    public static ClientId generate(){
        return new ClientId(UUID.randomUUID());
    }
}
