package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.GetClientAccountsUseCase;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.entities.helper.AccountFactory;
import com.banco.bank_system.useCase.clientUseCaseTests.helper.ClientFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetClientAccountsUseCaseTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private ClientRepositoryPort clientRepository;

    @InjectMocks
    private GetClientAccountsUseCase useCase;

    @Test
    void shouldReturnClientAccounts() {

        Client client = ClientFactory.create();

        List<Account> accounts = List.of(
                AccountFactory.checking(Clock.systemUTC()),
                AccountFactory.savings(Clock.systemUTC())
        );

        when(clientRepository.getClientByCpf(client.getCpf()))
                .thenReturn(Optional.of(client));

        when(accountRepository.getAccountsByClient(client.getId()))
                .thenReturn(accounts);

        GetClientAccountsOutput output = useCase.execute(client.getCpf());

        assertEquals(2, output.accountIdentities().size());

        verify(clientRepository)
                .getClientByCpf(client.getCpf());

        verify(accountRepository)
                .getAccountsByClient(client.getId());

        verifyNoMoreInteractions(clientRepository, accountRepository);
    }
}
