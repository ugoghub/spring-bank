package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.CPF;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetClientAccountsUseCase {

    private final AccountRepositoryPort accountRepository;
    private final ClientFinder clientFinder;


    public GetClientAccountsUseCase(
            AccountRepositoryPort accountRepository,
            ClientFinder clientFinder
    ) {
        this.accountRepository = accountRepository;
        this.clientFinder = clientFinder;
    }

    public GetClientAccountsOutput execute(CPF cpf){

        Client client = clientFinder.find(cpf);

        List<AccountIdentity> list = accountRepository
                .getAccountsByClient(client.getId())
                .stream()
                .map(Account::getAccountIdentity)
                .toList();

        return new GetClientAccountsOutput(list);
    }
}
