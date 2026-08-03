package com.banco.bank_system.application.client.util;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import org.springframework.stereotype.Component;

@Component
public class ClientFinder {

    private final ClientRepositoryPort clientRepository;

    public ClientFinder(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client find(CPF cpf){
        return clientRepository
                .getClientByCpf(cpf)
                .orElseThrow(ClientNotFoundException::new);
    }
}
