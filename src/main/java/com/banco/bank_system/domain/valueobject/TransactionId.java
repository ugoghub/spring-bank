package com.banco.bank_system.domain.valueobject;

import com.banco.bank_system.domain.exception.InvalidTransactionIdException;

import java.util.UUID;

public record TransactionId(UUID id) {

    public TransactionId{
        if(id == null) throw new InvalidTransactionIdException("ID do transação inválido");
    }

    public static TransactionId generate(){
        return new TransactionId(UUID.randomUUID());
    }

}
