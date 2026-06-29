package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;

import java.util.UUID;

public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(UUID clientId){
        Client client =
                clientRepository.findById(clientId)
                        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));


        clientRepository.delete(client.getId());
    }
}
