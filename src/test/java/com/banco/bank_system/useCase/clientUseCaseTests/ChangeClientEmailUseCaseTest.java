package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.ChangeClientEmailUseCase;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.exception.InvalidClientChangeException;
import com.banco.bank_system.domain.valueobject.Email;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ChangeClientEmailUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private ClientFinder clientFinder;

    @InjectMocks
    private ChangeClientEmailUseCase useCase;

    @Test
    void shouldChangeClientEmail() {

        Client client =
                ClientFactory.create();

        Email newEmail =
                new Email("novo_email@gmail.com");

        when(clientFinder.find(client.getCpf()))
                .thenReturn(client);

        GetClientDataOutput output =
                useCase.execute(
                        client.getCpf(),
                        newEmail
                );

        assertEquals(
                newEmail,
                output.email()
        );

        verify(clientFinder)
                .find(client.getCpf());

        verify(clientRepository)
                .save(client);

        verifyNoMoreInteractions(clientRepository);
    }
}
