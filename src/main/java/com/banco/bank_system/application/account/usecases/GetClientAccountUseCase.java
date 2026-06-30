package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GetClientAccountUseCase {

    private final AccountRepositoryPort accountRepository;


    public GetClientAccountUseCase(AccountRepositoryPort accountRepository) {
        this.accountRepository = accountRepository;
    }

    public GetClientAccountOutput execute(AccountIdentity accountIdentity){

        Account account = accountRepository.getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(AccountNotFoundException::new);

        return new GetClientAccountOutput(
              account.getId(),
                account.getClientId(),
                account.getAccountIdentity(),
                account.getCreationTime(),
                account.getBalance()
        );
    }
}
