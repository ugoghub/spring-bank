package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.springframework.stereotype.Service;

@Service
public class ChangeClientNameUseCase {

    private final ClientRepositoryPort clientRepository;

    private final ClientFinder clientFinder;

    public ChangeClientNameUseCase(ClientRepositoryPort clientRepository,
                                    ClientFinder clientFinder) {
        this.clientRepository = clientRepository;
        this.clientFinder = clientFinder;
    }

    public GetClientDataOutput execute(
            CPF cpf,
            PersonName newName
    ){

        Client client = clientFinder.find(cpf);

        client.changeName(newName);

        clientRepository.save(client);

        return new GetClientDataOutput(
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );
    }
}
