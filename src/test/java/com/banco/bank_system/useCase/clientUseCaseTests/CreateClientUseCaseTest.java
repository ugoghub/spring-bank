package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.CreateClientOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.CreateClientUseCase;
import com.banco.bank_system.application.client.util.ClientUniquenessValidator;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.domain.valueobject.PersonName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CreateClientUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private ClientUniquenessValidator validator;

    @InjectMocks
    private CreateClientUseCase useCase;

    @Test
    void shouldCreateClient() {

        PersonName name = new PersonName("Hugo Silva");
        CPF cpf = new CPF("52998224725");
        Email email = new Email("hugo@gmail.com");

        CreateClientOutput output =
                useCase.execute(name, cpf, email);

        verify(validator)
                .validate(cpf, email);

        verify(clientRepository)
                .save(any(Client.class));

        assertEquals(name, output.name());
        assertEquals(cpf, output.cpf());
        assertEquals(email, output.email());
    }
}
