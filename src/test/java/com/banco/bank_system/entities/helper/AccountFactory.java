package com.banco.bank_system.entities.helper;


import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.valueobject.AccountIdentityFactory;
import com.banco.bank_system.domain.valueobject.ClientId;

import java.time.Clock;

public final class AccountFactory {

    private AccountFactory() {
    }

    public static CheckingAccount checking(Clock clock) {
        return checking(ClientId.generate(), clock);
    }

    public static CheckingAccount checking(
            ClientId clientId,
            Clock clock
    ) {
        return CheckingAccount.create(
                clientId,
                AccountIdentityFactory.generate(),
                clock
        );
    }

    public static SavingsAccount savings(Clock clock) {
        return savings(ClientId.generate(), clock);
    }

    public static SavingsAccount savings(
            ClientId clientId,
            Clock clock
    ) {
        return SavingsAccount.create(
                clientId,
                AccountIdentityFactory.generate(),
                clock
        );
    }
}
