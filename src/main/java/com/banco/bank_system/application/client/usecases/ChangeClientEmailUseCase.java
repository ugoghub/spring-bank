package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;

public class ChangeClientEmailUseCase {

    private final ClientRepositoryPort clientRepository;

    public ChangeClientEmailUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Email execute(
            CPF cpf,
            Email newEmail
    ){

        Client client =
                clientRepository.getClientByCpf(cpf);

        if(clientRepository.existsByEmail(newEmail)){

            throw new IllegalArgumentException(
                    "Email já cadastrado"
            );
        }

        client.changeEmail(newEmail);

        clientRepository.save(client);

        return client.getEmail();

    }
}
