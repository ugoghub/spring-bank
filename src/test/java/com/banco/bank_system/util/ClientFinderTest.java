package com.banco.bank_system.util;

import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientFinderTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private ClientFinder clientFinder;

    @Test
    void shouldReturnClient() {

        Client client = ClientFactory.create();

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        Client result = clientFinder.find(client.getCpf());

        assertSame(client, result);

        verify(clientRepository)
                .getClientByCpf(client.getCpf());

        verifyNoMoreInteractions(clientRepository);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        CPF cpf = new CPF("52998224725");

        when(clientRepository.getClientByCpf(cpf))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> clientFinder.find(cpf)
        );

        verify(clientRepository)
                .getClientByCpf(cpf);

        verifyNoMoreInteractions(clientRepository);
    }
}
