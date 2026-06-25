package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;

import java.util.UUID;

public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(UUID clientId){
        if (!clientRepository.existsById(clientId)) throw new IllegalArgumentException("Cliente não encontrado");

        clientRepository.delete(clientId);
    }
}
