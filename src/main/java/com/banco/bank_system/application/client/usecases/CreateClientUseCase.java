package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;

public class CreateClientUseCase {

    private final ClientRepositoryPort clientRepository;

    public CreateClientUseCase(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void execute(
            PersonName name,
            CPF cpf,
            Email email
    ){
        validateCpfUniqueness(cpf);
        validateEmailUniqueness(email);

        Client client = new Client(name, cpf, email);

        clientRepository.save(client);
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
