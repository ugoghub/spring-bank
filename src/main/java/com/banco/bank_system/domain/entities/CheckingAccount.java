package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

public class CheckingAccount extends Account {

    private static final Money OVERDRAFT_LIMIT =
            Money.of("1000");

    private CheckingAccount(
            UUID id,
            ClientId clientId,
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

    private CheckingAccount(
            ClientId clientId,
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

    public static CheckingAccount create(
            ClientId clientId,
            AccountIdentity accountIdentity,
            Clock clock
    ){
        return new CheckingAccount(clientId, accountIdentity, clock);
    }

    public static CheckingAccount restore(
            UUID id,
            ClientId clientId,
            AccountIdentity accountIdentity,
            Money balance,
            LocalDateTime creationTime
    ){
        return new CheckingAccount(id, clientId, accountIdentity, balance, creationTime);
    }

    @Override
    protected Money minimumAllowedBalance() {
        return OVERDRAFT_LIMIT.negate();
    }
}
