package com.banco.bank_system.domain.valueobject;

import com.banco.bank_system.domain.exception.InvalidAccountIdException;

import java.util.UUID;

public record AccountId(UUID id) {

    public AccountId{
        if(id == null) throw new InvalidAccountIdException("ID do conta inválido");
    }

    public static AccountId generate(){
        return new AccountId(UUID.randomUUID());
    }
}
