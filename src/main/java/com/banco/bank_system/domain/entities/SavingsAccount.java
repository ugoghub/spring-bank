package com.banco.bank_system.domain.entities;

import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SavingsAccount extends Account {

    private static final BigDecimal INTEREST_RATE =
            new BigDecimal("0.005");

    private LocalDateTime lastInterestAppliedAt;

    private SavingsAccount(
            AccountId id,
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

        this.lastInterestAppliedAt = creationTime;
    }

    private SavingsAccount(
            ClientId clientId,
            AccountIdentity accountIdentity,
            Clock clock
    ){

        super(
                AccountId.generate(),
                clientId,
                accountIdentity,
                Money.ZERO,
                LocalDateTime.now(clock)
        );

        this.lastInterestAppliedAt = LocalDateTime.now(clock);
    }

    public static SavingsAccount create(
            ClientId clientId,
            AccountIdentity accountIdentity,
            Clock clock
    ){
        return new SavingsAccount(clientId, accountIdentity, clock);
    }

    public static SavingsAccount restore(
            AccountId id,
            ClientId clientId,
            AccountIdentity accountIdentity,
            Money balance,
            LocalDateTime creationTime
    ){
        return new SavingsAccount(id, clientId, accountIdentity, balance, creationTime);
    }

    @Override
    protected Money minimumAllowedBalance() {
        return Money.ZERO;
    }

    private boolean isTimeToApplyInterest(Clock clock) {

        return !lastInterestAppliedAt
                .plusMonths(1)
                .isAfter(LocalDateTime.now(clock));
    }

    public List<Money> applyPendingInterests(Clock clock) {

        List<Money> appliedInterests = new ArrayList<>();

        while (isTimeToApplyInterest(clock)) {

            if (getBalance().isGreaterThan(Money.ZERO)) {

                Money interest =
                        getBalance().multiplyByRate(INTEREST_RATE);

                increaseBalance(interest);

                appliedInterests.add(interest);
            }

            lastInterestAppliedAt =
                    lastInterestAppliedAt.plusMonths(1);
        }

        return appliedInterests;
    }
}
