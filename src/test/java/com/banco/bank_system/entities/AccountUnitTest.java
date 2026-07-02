package com.banco.bank_system.entities;

import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.exception.InvalidAccountIdentityException;
import com.banco.bank_system.domain.exception.InvalidClientIdException;
import com.banco.bank_system.domain.exception.InvalidMoneyException;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class AccountUnitTest {

    private final Clock clock =
            Clock.systemUTC();

    // =========================
    // General
    // =========================

    @Test
    void shouldStartWithZeroBalance() {

        CheckingAccount account = AccountFactory.checking(clock);

        assertEquals(
                Money.ZERO,
                account.getBalance()
        );
    }

    @Test
    void shouldRegisterCreationTime() {

        Clock fixedClock =
                Clock.fixed(
                        Instant.parse("2026-01-10T10:00:00Z"),
                        ZoneOffset.UTC
                );

        SavingsAccount account = AccountFactory.savings(fixedClock);

        assertEquals(
                LocalDateTime.of(
                        2026,
                        1,
                        10,
                        10,
                        0
                ),
                account.getCreationTime()
        );
    }

    // =========================
    // Validation
    // =========================

    @Test
    void shouldThrowExceptionWhenCreatingAccountWithNullClientId() {

        assertThrows(
                InvalidClientIdException.class,
                () -> CheckingAccount.create(
                        null,
                        new AccountIdentity("01", "123456-1"),
                        Clock.systemUTC()
                )
        );
    }

    @Test
    void shouldThrowExceptionWhenCreatingAccountWithNullAccountIdentity() {

        assertThrows(
                InvalidAccountIdentityException.class,
                () -> CheckingAccount.create(
                        ClientId.generate(),
                        null,
                        Clock.systemUTC()
                )
        );
    }

    // =========================
    // Delete
    // =========================

    @Test
    void shouldAllowRemovalWhenBalanceIsZero() {

        CheckingAccount account = AccountFactory.checking(clock);

        assertTrue(account.isRemovable());
    }

    @Test
    void shouldNotAllowRemovalWhenBalanceIsNotZero() {

        SavingsAccount account = AccountFactory.savings(clock);

        account.deposit(Money.of("1"));

        assertFalse(account.isRemovable());
    }

    @Test
    void shouldAllowRemovingAccountAfterReturningToZeroBalance() {

        CheckingAccount account = AccountFactory.checking(clock);

        account.deposit(
                Money.of("100")
        );

        account.withdraw(
                Money.of("100")
        );

        assertTrue(
                account.isRemovable()
        );
    }

    // =========================
    // Deposit
    // =========================

    @Test
    void shouldDepositMoney() {

        CheckingAccount account = AccountFactory.checking(clock);

        account.deposit(
                Money.of("100")
        );

        assertEquals(
                Money.of("100.00"),
                account.getBalance()
        );
    }

    @Test
    void shouldNotAllowNegativeDeposit() {

        SavingsAccount account = AccountFactory.savings(clock);

        assertThrows(
                InvalidMoneyException.class,
                () -> account.deposit(
                        Money.of("-10")
                )
        );
    }

    @Test
    void shouldNotAllowZeroDeposit() {

        CheckingAccount account = AccountFactory.checking(clock);

        assertThrows(
                InvalidMoneyException.class,
                () -> account.deposit(Money.ZERO)
        );
    }

    // =========================
    // Withdraw
    // =========================

    @Test
    void shouldNotAllowZeroWithdraw() {

        SavingsAccount account =
                AccountFactory.savings(clock);

        assertThrows(
                InvalidMoneyException.class,
                () -> account.withdraw(
                        Money.ZERO
                )
        );
    }

    @Test
    void shouldNotAllowNegativeWithdraw() {

        CheckingAccount account = AccountFactory.checking(clock);

        assertThrows(
                InvalidMoneyException.class,
                () -> account.withdraw(
                        Money.of("-1")
                )
        );
    }

    @Test
    void shouldWithdrawMoney() {

        SavingsAccount account = AccountFactory.savings(clock);

        account.deposit(
                Money.of("100")
        );

        account.withdraw(
                Money.of("40")
        );

        assertEquals(
                Money.of("60.00"),
                account.getBalance()
        );
    }
}
