package com.banco.bank_system.helper;


import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;

import java.time.Clock;
import java.util.UUID;

public final class AccountFactory {

    private AccountFactory() {
    }

    public static CheckingAccount checking(Clock clock) {
        return CheckingAccount.create(
                ClientId.generete(),
                new AccountIdentity("01", "123456-1"),
                clock
        );
    }

    public static SavingsAccount savings(Clock clock) {
        return SavingsAccount.create(
                ClientId.generete(),
                new AccountIdentity("01", "123456-1"),
                clock
        );
    }
}
