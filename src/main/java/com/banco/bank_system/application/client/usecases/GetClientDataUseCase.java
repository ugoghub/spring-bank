package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetClientDataUseCase {

    private final ClientRepositoryPort clientRepository;

    public GetClientDataUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public GetClientDataOutput execute(CPF cpf){
        Client client = clientRepository.getClientByCpf(cpf)
                .orElseThrow(ClientNotFoundException::new);

        return new GetClientDataOutput(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
