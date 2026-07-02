package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.usecases.GetAccountBalanceUseCase;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class GetAccountBalanceUseCaseTest {

    @Mock
    private AccountRepositoryPort repository;

    @InjectMocks
    private GetAccountBalanceUseCase useCase;

    @Test
    void shouldReturnBalance() {

        Account account =
                AccountFactory.checking(Clock.systemUTC());

        account.deposit(Money.of("500"));

        when(repository.getAccountByAccountIdentity(
                account.getAccountIdentity()))
                .thenReturn(Optional.of(account));

        GetBalanceOutput output =
                useCase.execute(account.getAccountIdentity());

        assertEquals(
                Money.of("500"),
                output.balance()
        );
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        AccountIdentity identity =
                new AccountIdentity("01", "123456-1");

        when(repository.getAccountByAccountIdentity(identity))
                .thenReturn(Optional.empty());

        assertThrows(
                AccountNotFoundException.class,
                () -> useCase.execute(identity)
        );
    }
}
