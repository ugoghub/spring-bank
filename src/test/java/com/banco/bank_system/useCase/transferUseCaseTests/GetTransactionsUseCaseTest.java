package com.banco.bank_system.useCase.transferUseCaseTests;

import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.application.transaction.usecases.GetTransactionsUseCase;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetTransactionsUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AccountFinder accountFinder;

    @InjectMocks
    private GetTransactionsUseCase useCase;

    private Clock clock;

    @BeforeEach
    void setup() {
        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );
    }

    @Test
    void shouldReturnTransactions() {

        // Arrange
        CheckingAccount account =
                AccountFactory.checking(clock);

        Transaction deposit =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("500"),
                        clock
                );

        Transaction withdraw =
                Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("200"),
                        clock
                );

        List<Transaction> transactions =
                List.of(deposit, withdraw);

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        when(transactionRepository.findByAccountId(account.getId()))
                .thenReturn(transactions);

        // Act
        List<TransactionDTO> output =
                useCase.execute(account.getAccountIdentity());

        // Assert
        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verify(transactionRepository)
                .findByAccountId(account.getId());

        assertEquals(2, output.size());

        assertEquals(
                TransactionType.DEPOSIT,
                output.getFirst().type()
        );

        assertEquals(
                TransactionType.WITHDRAW,
                output.getLast().type()
        );

        assertEquals(
                Money.of("500"),
                output.getFirst().amount()
        );

        assertEquals(
                Money.of("200"),
                output.getLast().amount()
        );
    }

    @Test
    void shouldReturnEmptyListWhenAccountHasNoTransactions() {

        // Arrange
        CheckingAccount account =
                AccountFactory.checking(clock);

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        when(transactionRepository.findByAccountId(account.getId()))
                .thenReturn(List.of());

        // Act
        List<TransactionDTO> output =
                useCase.execute(account.getAccountIdentity());

        // Assert
        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verify(transactionRepository)
                .findByAccountId(account.getId());

        assertTrue(output.isEmpty());
    }

    @Test
    void shouldThrowExceptionWhenAccountDoesNotExist() {

        // Arrange
        AccountIdentity identity =
                new AccountIdentity("01", "123456-1");

        when(accountFinder.byIdentity(identity))
                .thenThrow(new AccountNotFoundException());

        // Assert
        assertThrows(
                AccountNotFoundException.class,
                () -> useCase.execute(identity)
        );

        verify(accountFinder)
                .byIdentity(identity);

        verify(transactionRepository, never())
                .findByAccountId(any());

        verifyNoMoreInteractions(accountFinder);
        verifyNoMoreInteractions(transactionRepository);
    }
}
