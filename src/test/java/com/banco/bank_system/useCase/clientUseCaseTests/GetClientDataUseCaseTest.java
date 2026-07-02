package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.GetClientDataUseCase;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetClientDataUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private GetClientDataUseCase useCase;

    @Test
    void shouldReturnClientData() {

        Client client = ClientFactory.create();

        when(clientRepository.getClientByCpf(client.getCpf())).thenReturn(Optional.of(client));

        GetClientDataOutput output = useCase.execute(client.getCpf());

        assertEquals(
                client.getName(),
                output.name()
        );
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        CPF cpf =
                new CPF("52998224725");

        when(clientRepository.getClientByCpf(cpf))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> useCase.execute(cpf)
        );
    }
}
