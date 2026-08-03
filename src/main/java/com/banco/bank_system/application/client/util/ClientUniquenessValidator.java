package com.banco.bank_system.application.client.util;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.CpfAlreadyExistsException;
import com.banco.bank_system.application.exception.EmailAlreadyExistsException;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import org.springframework.stereotype.Component;

@Component
public class ClientUniquenessValidator {

    private final ClientRepositoryPort clientRepository;

    public ClientUniquenessValidator(ClientRepositoryPort clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void validate(CPF cpf, Email email) {

        if (clientRepository.existsByCpf(cpf)) {
            throw new CpfAlreadyExistsException();
        }

        if (clientRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException();
        }
    }
}
