package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;

public class GetAccountBalanceUseCase {

    private final AccountRepositoryPort accountRepository;

    public GetAccountBalanceUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Money execute(AccountIdentity accountIdentity) {
        return accountRepository.getAccountByAccountIdentity(accountIdentity)
                .map(Account::getBalance)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada para a identidade informada."));
    }
}