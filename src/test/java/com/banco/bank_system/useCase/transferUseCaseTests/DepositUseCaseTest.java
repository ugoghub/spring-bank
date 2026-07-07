package com.banco.bank_system.useCase.transferUseCaseTests;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.application.transaction.usecases.DepositUseCase;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DepositUseCaseTest {

    @Mock
    private TransactionRepositoryPort transactionRepository;

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private AccountFinder accountFinder;

    private DepositUseCase useCase;

    private Clock clock;

    @BeforeEach
    void setup() {

        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );

        useCase = new DepositUseCase(
                transactionRepository,
                accountRepository,
                accountFinder,
                clock
        );
    }

    @Test
    void shouldDeposit() {

        // Arrange

        CheckingAccount account =
                AccountFactory.checking(clock);

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        // Act

        DepositOutput output =
                useCase.execute(
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

        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(Money.of("500"), transaction.getAmount());
        assertEquals(account.getId(), transaction.getAccountId());
        assertEquals(account.getAccountIdentity(), transaction.getDestination());
        assertNull(transaction.getSource());
        assertEquals(
                LocalDateTime.of(2026, 1, 10, 10, 0),
                transaction.getDateTime()
        );

        assertEquals(Money.of("500"), output.depositedAmount());
        assertEquals(Money.of("500"), account.getBalance());
    }
}
