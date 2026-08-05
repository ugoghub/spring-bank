package com.banco.bank_system.useCase.client;

import com.banco.bank_system.application.client.dto.GetClientDataOutput;
import com.banco.bank_system.application.client.usecases.GetClientDataUseCase;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetClientDataUseCaseTest {

    @Mock
    private ClientFinder clientFinder;

    @InjectMocks
    private GetClientDataUseCase useCase;

    @Test
    void shouldReturnClientData() {

        Client client = ClientFactory.create();

        when(clientFinder.find(client.getCpf()))
                .thenReturn(client);

        GetClientDataOutput output = useCase.execute(client.getCpf());

        assertEquals(
                client.getName(),
                output.name()
        );

        assertEquals(
                client.getEmail(),
                output.email()
        );

        assertEquals(
                client.getCpf(),
                output.cpf()
        );

        verify(clientFinder)
                .find(client.getCpf());
    }
}
