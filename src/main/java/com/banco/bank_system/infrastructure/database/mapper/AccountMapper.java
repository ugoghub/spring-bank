package com.banco.bank_system.infrastructure.database.mapper;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.valueobject.AccountId;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.ClientId;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public final class AccountMapper {

    private AccountMapper() {
    }

    public static Account toDomain(AccountEntity entity) {

        AccountIdentity identity =
                new AccountIdentity(
                        entity.getBranch(),
                        entity.getAccountNumber()
                );

        Money money = Money.of(entity.getBalance());

        return switch (entity.getAccountType()) {

            case CHECKING -> CheckingAccount.restore(
                    new AccountId(entity.getId()),
                    new ClientId(entity.getClientId()),
                    identity,
                    money,
                    entity.getCreatedAt()
            );

            case SAVINGS -> SavingsAccount.restore(
                    new AccountId(entity.getId()),
                    new ClientId(entity.getClientId()),
                    identity,
                    money,
                    entity.getCreatedAt(),
                    entity.getLastInterestAppliedAt()
            );
        };
    }

    public static AccountEntity fromDomain(Account account) {

        LocalDateTime lastInterestAppliedAt = null;

        if(account instanceof SavingsAccount savings){
            lastInterestAppliedAt = savings.getLastInterestAppliedAt();
        }

        return new AccountEntity(
                account.getId().id(),
                account.getClientId().id(),
                account.getAccountIdentity().accountNumber(),
                account.getAccountIdentity().branch(),
                account.getBalance().value(),
                account.getType(),
                account.getCreationTime(),
                lastInterestAppliedAt
        );
    }
}
