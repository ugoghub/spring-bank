package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.RemoveAccountUseCase;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RemoveAccountUseCaseTest {
    @Mock
    private AccountRepositoryPort repository;

    @Mock
    private AccountFinder accountFinder;

    @InjectMocks
    private RemoveAccountUseCase useCase;

    @Test
    void shouldRemoveAccount() {

        Account account = AccountFactory.checking(Clock.systemUTC());

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        useCase.execute(account.getAccountIdentity());

        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verify(repository)
                .delete(account.getId());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void shouldNotRemoveAccountWithActiveBalance() {

        Account account =
                AccountFactory.checking(Clock.systemUTC());

        account.deposit(Money.of("100"));

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        assertThrows(
                CannotRemoveAccountException.class,
                () -> useCase.execute(account.getAccountIdentity())
        );

        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verify(repository, never())
                .delete(any());

        verifyNoMoreInteractions(repository, accountFinder);
    }
}
