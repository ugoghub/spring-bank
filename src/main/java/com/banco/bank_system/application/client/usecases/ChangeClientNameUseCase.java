package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.output.ChangeClientNameOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.springframework.stereotype.Service;

@Service
public class ChangeClientNameUseCase {

    private final ClientRepositoryPort clientRepository;

    public ChangeClientNameUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ChangeClientNameOutput execute(
            CPF cpf,
            PersonName newName
    ){
        Client client =
                clientRepository.getClientByCpf(cpf)
                        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        client.changeName(newName);

        clientRepository.save(client);

        return new ChangeClientNameOutput(client.getName());
    }
}
