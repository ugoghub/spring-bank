package com.banco.bank_system.useCase.clientUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.usecases.RemoveClientUseCase;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoveClientUseCaseTest {

    @Mock
    private ClientRepositoryPort clientRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private RemoveClientUseCase useCase;

    private Client client;


    @BeforeEach
    void setup() {
        client = ClientFactory.create();
    }

    @Test
    void shouldRemoveClient() {

        List<Account> accounts = List.of(
                AccountFactory.checking(Clock.systemUTC()),
                AccountFactory.savings(Clock.systemUTC())
        );

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        when(accountRepository.getAccountsByClient(client.getId()))
                .thenReturn(accounts);

        // Act
        useCase.execute(client.getCpf());

        // Assert
        verify(clientRepository)
                .getClientByCpf(client.getCpf());

        verify(accountRepository)
                .getAccountsByClient(client.getId());

        verify(accountRepository)
                .removeClientAccounts(client.getId());

        verify(clientRepository)
                .delete(client.getId());

        verifyNoMoreInteractions(clientRepository);
        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldThrowExceptionWhenClientDoesNotExist() {

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.empty());

        assertThrows(
                ClientNotFoundException.class,
                () -> useCase.execute(client.getCpf())
        );

        verify(accountRepository, never())
                .removeClientAccounts(any());

        verify(clientRepository, never())
                .delete(any());
    }

    @Test
    void shouldThrowExceptionWhenClientAccountHasActiveBalance() {

        CheckingAccount account =
                AccountFactory.checking(Clock.systemUTC());

        account.deposit(Money.of("100"));

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        when(accountRepository.getAccountsByClient(client.getId()))
                .thenReturn(List.of(account));

        assertThrows(
                CannotRemoveAccountException.class,
                () -> useCase.execute(client.getCpf())
        );

        verify(accountRepository, never())
                .removeClientAccounts(any());

        verify(clientRepository, never())
                .delete(any());
    }
}
