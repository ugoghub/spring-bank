package com.banco.bank_system.useCase.account;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.CreateAccountUseCase;
import com.banco.bank_system.application.account.util.UniqueAccountIdentityGenerator;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.useCase.client.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateAccountUseCaseTest {

    @Mock
    private ClientFinder clientFinder;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private UniqueAccountIdentityGenerator uniqueAccountIdentityGenerator;

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
                clientFinder,
                uniqueAccountIdentityGenerator,
                clock);
    }

    @Test
    void shouldCreateCheckingAccount() {

        when(clientFinder.find(client.getCpf()))
                .thenReturn(client);

        when(uniqueAccountIdentityGenerator.generate())
                .thenReturn(
                        new AccountIdentity("01", "123456-1")
                );

        CreateAccountOutput output =
                useCase.execute(client.getCpf(), AccountType.CHECKING);

        assertEquals(client.getId(), output.clientId());

        verify(clientFinder).find(client.getCpf());

        verify(uniqueAccountIdentityGenerator).generate();

        verify(accountRepository).save(any(Account.class));

        verifyNoMoreInteractions(accountRepository);
    }
}
