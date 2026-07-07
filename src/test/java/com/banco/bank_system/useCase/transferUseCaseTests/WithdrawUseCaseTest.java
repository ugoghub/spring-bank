package com.banco.bank_system.useCase.transferUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.application.transaction.usecases.WithdrawUseCase;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private AccountFinder accountFinder;

    private WithdrawUseCase useCase;

    private Clock clock;

    @BeforeEach
    void setup() {

        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );

        useCase = new WithdrawUseCase(
                transactionRepository,
                accountRepository,
                accountFinder,
                clock
        );
    }

    @Test
    void shouldWithdraw() {

        // Arrange

        CheckingAccount account =
                AccountFactory.checking(clock);

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        // Act


        WithdrawOutput output = useCase.execute(
                account.getAccountIdentity(),
                Money.of("500")
        );

        // Assert

        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verify(accountRepository)
                .save(account);

        ArgumentCaptor<Transaction> captor =
                ArgumentCaptor.forClass(Transaction.class);

        verify(transactionRepository)
                .save(captor.capture());

        Transaction transaction = captor.getValue();

        assertEquals(TransactionType.WITHDRAW, transaction.getType());
        assertEquals(Money.of("500"), transaction.getAmount());
        assertEquals(account.getId(), transaction.getAccountId());
        assertEquals(
                account.getAccountIdentity(),
                transaction.getSource()
        );

        assertNull(
                transaction.getDestination()
        );
        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                transaction.getDateTime()
        );

        assertEquals(Money.of("500"), output.withdrawnAmount());
        assertEquals(Money.of("-500"), account.getBalance());
        assertEquals(
                Money.of("500"),
                output.withdrawnAmount()
        );

        verifyNoMoreInteractions(accountFinder);
        verifyNoMoreInteractions(accountRepository);
        verifyNoMoreInteractions(transactionRepository);
    }
}
