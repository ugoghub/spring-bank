package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetBalanceOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.application.exception.AccountNotFoundException;

public class GetAccountBalanceUseCase {

    private final AccountRepositoryPort accountRepository;

    public GetAccountBalanceUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public GetBalanceOutput execute(AccountIdentity accountIdentity) {
        Money money = accountRepository.getAccountByAccountIdentity(accountIdentity)
                .map(Account::getBalance)
                .orElseThrow(AccountNotFoundException::new);

        return new GetBalanceOutput(money);
    }
}