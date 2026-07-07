package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.ChangeClientEmailUseCase;
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

    @InjectMocks
    private ChangeClientEmailUseCase useCase;

    @Test
    void shouldChangeClientName() {

        Client client =
                ClientFactory.create();

        Email newEmail =
                new Email("a@gmail.com");

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        GetClientDataOutput output =
                useCase.execute(
                        client.getCpf(),
                        newEmail
                );

        verify(clientRepository)
                .save(client);

        assertEquals(
                newEmail,
                output.email()
        );

        verify(clientRepository)
                .getClientByCpf(client.getCpf());

        verify(clientRepository)
                .save(client);

        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void shouldThrowExceptionWhenChangingEmailToCurrentEmail() {

        Client client =
                ClientFactory.create();

        when(clientRepository.getClientByCpf(client.getCpf())).thenReturn(Optional.of(client));

        assertThrows(
                InvalidClientChangeException.class,
                () -> useCase.execute(
                        client.getCpf(),
                        client.getEmail()
                )
        );
    }
}
