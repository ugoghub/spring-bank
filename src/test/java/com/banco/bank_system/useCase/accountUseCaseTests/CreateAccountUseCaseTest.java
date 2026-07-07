package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.CreateAccountUseCase;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    private CreateAccountUseCase useCase;

    private Client client;

    @BeforeEach
    void setup() {
        client = ClientFactory.create();

        Clock clock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneOffset.UTC
        );


        useCase = new CreateAccountUseCase(
                accountRepository,
                clientRepository,
                clock);
    }

    @Test
    void shouldCreateCheckingAccount() {

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        when(accountRepository.existsByAccountIdentity(any()))
                .thenReturn(false);

        CreateAccountOutput output =
                useCase.execute(client.getCpf(), AccountType.CHECKING);

        assertEquals(client.getId(), output.clientId());

        verify(clientRepository).getClientByCpf(client.getCpf());
        verify(accountRepository).existsByAccountIdentity(any());
        verify(accountRepository).save(any(Account.class));
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        when(clientRepository.getClientByCpf(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> useCase.execute(
                        new CPF("52998224725"),
                        AccountType.CHECKING
                )
        );

        verify(clientRepository).getClientByCpf(any());
        verify(accountRepository, never()).save(any());
        verify(accountRepository, never()).existsByAccountIdentity(any());
    }
}
