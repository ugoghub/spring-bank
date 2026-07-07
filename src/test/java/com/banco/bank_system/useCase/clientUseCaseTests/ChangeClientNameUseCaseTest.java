package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.ChangeClientEmailUseCase;
import com.banco.bank_system.application.client.usecases.ChangeClientNameUseCase;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.exception.InvalidClientChangeException;
import com.banco.bank_system.domain.valueobject.PersonName;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
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
public class ChangeClientNameUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private ChangeClientNameUseCase useCase;

    @Test
    void shouldChangeClientName() {

        Client client =
                ClientFactory.create();

        PersonName newName =
                new PersonName("Novo Nome");

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        GetClientDataOutput output =
                useCase.execute(
                        client.getCpf(),
                        newName
                );

        verify(clientRepository)
                .save(client);

        assertEquals(
                newName,
                output.name()
        );
    }

    @Test
    void shouldThrowExceptionWhenChangingNameToCurrentName() {

        Client client =
                ClientFactory.create();

        PersonName newName =
                new PersonName("Novo Nome");

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        GetClientDataOutput output =
                useCase.execute(
                        client.getCpf(),
                        newName
                );

        verify(clientRepository)
                .save(client);

        assertThrows(
                InvalidClientChangeException.class,
                () -> useCase.execute(
                        output.cpf(),
                        newName
                )
        );

        verify(clientRepository)
                .save(client);

        verifyNoMoreInteractions(clientRepository);
    }
}
