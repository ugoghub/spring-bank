package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.output.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import org.springframework.stereotype.Service;

@Service
public class GetClientDataUseCase {

    private final ClientRepositoryPort repository;

    public GetClientDataUseCase(ClientRepositoryPort clientRepository) {
        this.repository = clientRepository;
    }

    public GetClientDataOutput execute(CPF cpf){
        Client client = repository.getClientByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        return new GetClientDataOutput(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
