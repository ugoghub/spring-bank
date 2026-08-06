package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChangeClientEmailUseCase {

    private final ClientRepositoryPort clientRepository;

    private final ClientFinder clientFinder;

    public ChangeClientEmailUseCase(ClientRepositoryPort clientRepository,
                                    ClientFinder clientFinder) {
        this.clientRepository = clientRepository;
        this.clientFinder = clientFinder;
    }

    @Transactional
    public GetClientDataOutput execute(
            CPF cpf,
            Email newEmail
    ){
        Client client = clientFinder.find(cpf);

        client.changeEmail(newEmail);

        clientRepository.save(client);

        return new GetClientDataOutput(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
