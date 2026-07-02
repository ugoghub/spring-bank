package com.banco.bank_system.useCase.clientUseCaseTests.helper;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;

public final class ClientFactory {

    private ClientFactory() {
    }

    public static Client create() {
        return Client.create(
                new PersonName("pedro"),
                new CPF("883.039.380-02"),
                new Email("pedro@gmail.com")
        );
    }
}
