package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.PersonName;

public class ChangeClientNameUseCase {

    private final ClientRepositoryPort clientRepository;

    public ChangeClientNameUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public PersonName execute(CPF cpf, PersonName newName){
        Client client = clientRepository.getClientByCpf(cpf);

        if (client.hasSameName(newName)) throw new IllegalArgumentException("Novo nome é igual ao nome atual");

        client.changeName(newName);

        return client.getName();
    }
}
