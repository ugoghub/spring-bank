package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientUniquenessValidator;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateClientUseCase {

    private final ClientRepositoryPort clientRepository;
    private final ClientUniquenessValidator validator;

    public CreateClientUseCase(ClientRepositoryPort clientRepository,
                               ClientUniquenessValidator validator) {
        this.clientRepository = clientRepository;
        this.validator = validator;
    }

    @Transactional
    public CreateClientOutput execute(
            PersonName name,
            CPF cpf,
            Email email
    ){

        validator.validate(cpf, email);

        Client client = Client.create(name, cpf, email);

        clientRepository.save(client);

        return new CreateClientOutput(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );

    }
}
