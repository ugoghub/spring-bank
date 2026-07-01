package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetAccountBalanceUseCase {

    private final AccountFinder accountFinder;

    public GetAccountBalanceUseCase(AccountFinder accountFinder) {
        this.accountFinder = accountFinder;
    }

    public GetBalanceOutput execute(AccountIdentity accountIdentity) {
        Money money = accountFinder.byIdentity(accountIdentity).getBalance();

        return new GetBalanceOutput(money);
    }
}