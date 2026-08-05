package com.banco.bank_system.util;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountFinderTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private AccountFinder accountFinder;

    @Test
    void shouldReturnAccount() {

        Account account =
                AccountFactory.checking(Clock.systemUTC());

        AccountIdentity identity =
                account.getAccountIdentity();

        when(accountRepository.getAccountByAccountIdentity(identity))
                .thenReturn(Optional.of(account));

        Account result =
                accountFinder.byIdentity(identity);

        assertSame(account, result);

        verify(accountRepository)
                .getAccountByAccountIdentity(identity);

        verifyNoMoreInteractions(accountRepository);
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        AccountIdentity identity =
                new AccountIdentity("01", "123456-1");

        when(accountRepository.getAccountByAccountIdentity(identity))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> accountFinder.byIdentity(identity)
        );

        verify(accountRepository)
                .getAccountByAccountIdentity(identity);

        verifyNoMoreInteractions(accountRepository);
    }
}
