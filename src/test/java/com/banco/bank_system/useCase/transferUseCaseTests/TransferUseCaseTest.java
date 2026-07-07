package com.banco.bank_system.useCase.transferUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.InvalidTransferException;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.application.transaction.usecases.TransferUseCase;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InsufficientBalanceException;
import com.banco.bank_system.domain.exception.InvalidMoneyException;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private AccountFinder accountFinder;

    private TransferUseCase useCase;

    private Clock clock;

    @BeforeEach
    void setup() {

        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );

        useCase = new TransferUseCase(
                transactionRepository,
                accountRepository,
                accountFinder,
                clock
        );
    }

    @Test
    void shouldTransfer() {

        // Arrange
        CheckingAccount from =
                AccountFactory.checking(clock);

        SavingsAccount to =
                AccountFactory.savings(clock);

        Money value = Money.of("200");

        from.deposit(value);

        when(accountFinder.byIdentity(from.getAccountIdentity()))
                .thenReturn(from);

        when(accountFinder.byIdentity(to.getAccountIdentity()))
                .thenReturn(to);

        // Act
        TransferOutput output =
                useCase.execute(
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        value
                );

        // Assert - interactions

        verify(accountFinder)
                .byIdentity(from.getAccountIdentity());

        verify(accountFinder)
                .byIdentity(to.getAccountIdentity());

        verify(accountRepository)
                .save(from);

        verify(accountRepository)
                .save(to);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository, times(2))
                .save(captor.capture());

        List<Transaction> transactions =
                captor.getAllValues();

        Transaction sent = transactions.getFirst();
        Transaction received = transactions.getLast();

        // Assert - output

        assertEquals(output.operationId(), sent.getOperationId());
        assertEquals(output.operationId(), received.getOperationId());

        assertEquals(from.getAccountIdentity(), output.source());
        assertEquals(to.getAccountIdentity(), output.destination());

        assertEquals(Money.of("200"), output.amount());

        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                output.transactionDate()
        );

        // Assert - transactions

        assertEquals(TransactionType.TRANSFER_SENT, sent.getType());
        assertEquals(TransactionType.TRANSFER_RECEIVED, received.getType());

        assertEquals(Money.of("200"), sent.getAmount());
        assertEquals(Money.of("200"), received.getAmount());

        assertEquals(from.getId(), sent.getAccountId());
        assertEquals(to.getId(), received.getAccountId());

        assertEquals(from.getAccountIdentity(), sent.getSource());
        assertEquals(to.getAccountIdentity(), sent.getDestination());

        assertEquals(from.getAccountIdentity(), received.getSource());
        assertEquals(to.getAccountIdentity(), received.getDestination());

        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                sent.getDateTime()
        );

        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                received.getDateTime()
        );

        assertNotEquals(sent.getId(), received.getId());

        // Assert - account state

        assertEquals(Money.ZERO, from.getBalance());
        assertEquals(Money.of("200"), to.getBalance());

        verifyNoMoreInteractions(accountFinder);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(transactionRepository);
    }

    @Test
    void shouldThrowExceptionWhenTransferringToSameAccount() {

        CheckingAccount account =
                AccountFactory.checking(clock);

        account.deposit(Money.of("200"));

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        assertThrows(
                InvalidTransferException.class,
                () -> useCase.execute(
                        account.getAccountIdentity(),
                        account.getAccountIdentity(),
                        Money.of("200")
                )
        );
    }

    @Test
    void shouldNotAllowTransferWithInsufficientBalance() {

        SavingsAccount from = AccountFactory.savings(clock);
        CheckingAccount to = AccountFactory.checking(clock);

        when(accountFinder.byIdentity(from.getAccountIdentity()))
                .thenReturn(from);

        when(accountFinder.byIdentity(to.getAccountIdentity()))
                .thenReturn(to);

        assertThrows(
                InsufficientBalanceException.class,
                () -> useCase.execute(
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100")
                )
        );

        verify(accountFinder)
                .byIdentity(from.getAccountIdentity());

        verify(accountFinder)
                .byIdentity(to.getAccountIdentity());

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());

    }

    @Test
    void shouldNotAllowNegativeTransferAmount() {

        CheckingAccount from = AccountFactory.checking(clock);
        SavingsAccount to = AccountFactory.savings(clock);

        when(accountFinder.byIdentity(from.getAccountIdentity()))
                .thenReturn(from);

        when(accountFinder.byIdentity(to.getAccountIdentity()))
                .thenReturn(to);

        assertThrows(
                InvalidMoneyException.class,
                () -> useCase.execute(
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("-1")
                )
        );

        verify(accountFinder)
                .byIdentity(from.getAccountIdentity());

        verify(accountFinder)
                .byIdentity(to.getAccountIdentity());

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldNotAllowZeroTransferAmount() {

        CheckingAccount from = AccountFactory.checking(clock);
        SavingsAccount to = AccountFactory.savings(clock);

        when(accountFinder.byIdentity(from.getAccountIdentity()))
                .thenReturn(from);

        when(accountFinder.byIdentity(to.getAccountIdentity()))
                .thenReturn(to);

        assertThrows(
                InvalidMoneyException.class,
                () -> useCase.execute(
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.ZERO
                )
        );

        verify(accountFinder)
                .byIdentity(from.getAccountIdentity());

        verify(accountFinder)
                .byIdentity(to.getAccountIdentity());

        verify(accountRepository, never()).save(any());
        verify(transactionRepository, never()).save(any());
    }
}
