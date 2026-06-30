package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.output.ChangeClientEmailOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import org.springframework.stereotype.Service;

@Service
public class ChangeClientEmailUseCase {

    private final ClientRepositoryPort clientRepository;

    public ChangeClientEmailUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ChangeClientEmailOutput execute(
            CPF cpf,
            Email newEmail
    ){
        Client client =
                clientRepository.getClientByCpf(cpf)
                        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        client.changeEmail(newEmail);

        clientRepository.save(client);

        return new ChangeClientEmailOutput(client.getName());
    }
}
