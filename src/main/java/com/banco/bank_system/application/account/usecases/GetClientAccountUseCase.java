package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetClientAccountUseCase {

    private final AccountFinder accountFinder;

    public GetClientAccountUseCase(AccountFinder accountFinder) {
        this.accountFinder = accountFinder;
    }

    public GetClientAccountOutput execute(AccountIdentity accountIdentity) {

        Account account = accountFinder.byIdentity(accountIdentity);

        return new GetClientAccountOutput(
                account.getId(),
                account.getClientId(),
                account.getAccountIdentity(),
                account.getCreationTime(),
                account.getBalance()
        );
    }
}
