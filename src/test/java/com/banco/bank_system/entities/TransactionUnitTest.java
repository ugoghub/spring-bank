package com.banco.bank_system.entities;

import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.enums.TransactionType;
import com.banco.bank_system.domain.exception.InvalidTransactionException;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.domain.valueobject.OperationId;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionUnitTest {

    // =========================
    // Deposit
    // =========================

    @Test
    void shouldCreateDepositTransaction() {

        Clock clock = Clock.systemUTC();

        CheckingAccount account = AccountFactory.checking(clock);

        Transaction transaction =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("100"),
                        clock
                );

        assertEquals(
                TransactionType.DEPOSIT,
                transaction.getType()
        );

        assertNull(
                transaction.getSource()
        );

        assertEquals(
                account.getAccountIdentity(),
                transaction.getDestination()
        );
    }

    @Test
    void shouldGenerateUniqueIds() {

        SavingsAccount account = AccountFactory.savings(Clock.systemUTC());

        Transaction first =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("10"),
                        Clock.systemUTC()
                );

        Transaction second =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("10"),
                        Clock.systemUTC()
                );

        assertNotEquals(
                first.getId(),
                second.getId()
        );
    }

    @Test
    void shouldUseInjectedClock() {

        Clock fixed =
                Clock.fixed(
                        Instant.parse("2026-01-10T10:00:00Z"),
                        ZoneOffset.UTC
                );

        CheckingAccount account = AccountFactory.checking(Clock.systemUTC());

        Transaction transaction =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("100"),
                        fixed
                );

        assertEquals(
                LocalDateTime.of(
                        2026,
                        1,
                        10,
                        10,
                        0
                ),
                transaction.getDateTime()
        );
    }

    @Test
    void shouldStoreTransactionAmount() {

        SavingsAccount account = AccountFactory.savings(Clock.systemUTC());

        Transaction transaction =
                Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("250.50"),
                        Clock.systemUTC()
                );

        assertEquals(
                Money.of("250.50"),
                transaction.getAmount()
        );
    }

    @Test
    void depositShouldNotAllowDestinationAccountNull() {

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.deposit(
                        null,
                        null,
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowDepositNullAmount() {

        CheckingAccount account = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        null,
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowZeroDepositTransactionAmount() {

        SavingsAccount account = AccountFactory.savings(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.ZERO,
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowNegativeDepositTransactionAmount() {

        SavingsAccount account = AccountFactory.savings(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.deposit(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("-100"),
                        Clock.systemUTC()
                )
        );
    }

    // =========================
    // Withdraw
    // =========================

    @Test
    void shouldCreateWithdrawTransaction() {

        CheckingAccount account = AccountFactory.checking(Clock.systemUTC());

        Transaction transaction =
                Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("50"),
                        Clock.systemUTC()
                );

        assertEquals(
                TransactionType.WITHDRAW,
                transaction.getType()
        );

        assertEquals(
                account.getAccountIdentity(),
                transaction.getSource()
        );

        assertNull(
                transaction.getDestination()
        );
    }

    @Test
    void withdrawShouldNotAllowSourceAccountNull() {

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.withdraw(
                        null,
                        null,
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowWithdrawNullAmount() {

        CheckingAccount account = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        null,
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowZeroWithdrawTransactionAmount() {

        SavingsAccount account = AccountFactory.savings(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.ZERO,
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldNotAllowNegativeWithdrawTransactionAmount() {

        CheckingAccount account = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.withdraw(
                        account.getId(),
                        account.getAccountIdentity(),
                        Money.of("-100"),
                        Clock.systemUTC()
                )
        );
    }

    // =========================
    // Transfer
    // =========================

    @Test
    void shouldCreateTransferSentTransaction() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        OperationId operationId = OperationId.generate();

        Transaction transaction =
                Transaction.transferSent(
                        from.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                );

        assertEquals(
                TransactionType.TRANSFER_SENT,
                transaction.getType()
        );

        assertEquals(
                from.getAccountIdentity(),
                transaction.getSource()
        );

        assertEquals(
                to.getAccountIdentity(),
                transaction.getDestination()
        );
    }

    @Test
    void shouldCreateTransferReceivedTransaction() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        OperationId operationId = OperationId.generate();

        Transaction transaction =
                Transaction.transferReceived(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                );

        assertEquals(
                TransactionType.TRANSFER_RECEIVED,
                transaction.getType()
        );

        assertEquals(
                from.getAccountIdentity(),
                transaction.getSource()
        );

        assertEquals(
                to.getAccountIdentity(),
                transaction.getDestination()
        );

        assertEquals(
                operationId,
                transaction.getOperationId()
        );
    }

    @Test
    void shouldGenerateDifferentIdsForTransferPair() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        OperationId operationId = OperationId.generate();

        Transaction sent =
                Transaction.transferSent(
                        from.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                );

        Transaction received =
                Transaction.transferReceived(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                );

        assertNotEquals(
                sent.getId(),
                received.getId()
        );

        assertEquals(
                sent.getOperationId(),
                received.getOperationId()
        );
    }

    @Test
    void transferSentShouldNotAllowNullAccounts() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        OperationId operationId = OperationId.generate();

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferSent(
                        from.getId(),
                        operationId,
                        null,
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferSent(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        null,
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void transferReceivedShouldNotAllowNullAccounts() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        OperationId operationId = OperationId.generate();

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferReceived(
                        from.getId(),
                        operationId,
                        null,
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferReceived(
                        to.getId(),
                        operationId,
                        from.getAccountIdentity(),
                        null,
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void transferShouldNotAllowNullOperationId() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());
        CheckingAccount to = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferSent(
                        from.getId(),
                        null,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.transferReceived(
                        from.getId(),
                        null,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        Money.of("100"),
                        Clock.systemUTC()
                )
        );
    }

    // =========================
    // Interest
    // =========================

    @Test
    void shouldCreateInterestTransaction() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());

        Transaction transaction =
                Transaction.interest(
                        from.getId(),
                        from.getAccountIdentity(),
                        Money.of("5"),
                        Clock.systemUTC()
                );

        assertEquals(
                TransactionType.INTEREST,
                transaction.getType()
        );

        assertNull(
                transaction.getSource()
        );

        assertEquals(
                from.getAccountIdentity(),
                transaction.getDestination()
        );
    }

    @Test
    void shouldThrowExceptionWhenInterestHasNullClock() {
        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.interest(
                        from.getId(),
                        from.getAccountIdentity(),
                        Money.of("5"),
                        null
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenInterestHasNullAccountIdentity() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.interest(
                        from.getId(),
                        null,
                        Money.of("5"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenInterestHasNullAmount() {

        CheckingAccount from = AccountFactory.checking(Clock.systemUTC());

        assertThrows(
                InvalidTransactionException.class,
                () -> Transaction.interest(
                        from.getId(),
                        from.getAccountIdentity(),
                        null,
                        Clock.systemUTC()
                )
        );
    }
}
