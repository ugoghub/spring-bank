package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.dto.output.CreateClientOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CreateClientUseCase {

    private final ClientRepositoryPort clientRepository;

    public CreateClientUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public CreateClientOutput execute(
            PersonName name,
            CPF cpf,
            Email email
    ){
        validateCpfUniqueness(cpf);
        validateEmailUniqueness(email);

        Client client = new Client(UUID.randomUUID(), name, cpf, email);

        clientRepository.save(client);

        return new CreateClientOutput(
                client.getId(),
                client.getName(),
                client.getCpf(),
                client.getEmail()
        );

    }

    private void validateCpfUniqueness(CPF cpf) {

        if (clientRepository.existsByCpf(cpf)) {
            throw new IllegalArgumentException(
                    "CPF já cadastrado"
            );
        }
    }

    private void validateEmailUniqueness(Email email) {

        if (clientRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email já cadastrado"
            );
        }
    }
}
