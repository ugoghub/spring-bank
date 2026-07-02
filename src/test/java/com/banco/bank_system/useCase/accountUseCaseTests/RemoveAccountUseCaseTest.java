package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.RemoveAccountUseCase;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class RemoveAccountUseCaseTest {
    @Mock
    private AccountRepositoryPort repository;

    @InjectMocks
    private RemoveAccountUseCase useCase;

    @Test
    void shouldRemoveAccount() {

        Account account = AccountFactory.checking(Clock.systemUTC());

        when(repository.getAccountByAccountIdentity(
                account.getAccountIdentity()))
                .thenReturn(Optional.of(account));

        useCase.execute(account.getAccountIdentity());

        verify(repository)
                .delete(account.getId());
    }

    @Test
    void shouldNotRemoveAccountWithActiveBalance() {

        Account account =
                AccountFactory.checking(Clock.systemUTC());

        account.deposit(Money.of("100"));

        when(repository.getAccountByAccountIdentity(
                account.getAccountIdentity()))
                .thenReturn(Optional.of(account));

        assertThrows(
                CannotRemoveAccountException.class,
                () -> useCase.execute(account.getAccountIdentity())
        );

        verify(repository, never())
                .delete(any());
    }
}
