package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class CheckingAccount extends Account {

    private static final Money OVERDRAFT_LIMIT =
            Money.of("1000");

    public CheckingAccount(
            UUID id,
            UUID clientId,
            AccountIdentity accountIdentity,
            Money balance,
            LocalDateTime creationTime
    ) {

        super(
                id,
                clientId,
                accountIdentity,
                balance,
                creationTime
        );
    }

    public CheckingAccount(
            UUID clientId,
            AccountIdentity accountIdentity,
            Clock clock
    ){

        super(
                UUID.randomUUID(),
                clientId,
                accountIdentity,
                Money.ZERO,
                LocalDateTime.now(clock)
        );

    }

    @Override
    protected Money minimumAllowedBalance() {
        return OVERDRAFT_LIMIT.negate();
    }
}
