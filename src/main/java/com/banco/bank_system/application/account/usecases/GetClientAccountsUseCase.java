package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.GetClientAccountsOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.CPF;

import java.util.List;

public class GetClientAccountsUseCase {

    private final AccountRepositoryPort accountRepository;
    private final ClientRepositoryPort clientRepository;


    public GetClientAccountsUseCase(
            AccountRepositoryPort accountRepository,
            ClientRepositoryPort clientRepository
    ) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    public GetClientAccountsOutput execute(CPF cpf){

        Client client = clientRepository.getClientByCpf(cpf)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        List<AccountIdentity> list = accountRepository
                .getAccountsByClient(client.getId())
                .stream()
                .map(Account::getAccountIdentity)
                .toList();

        return new GetClientAccountsOutput(list);
    }
}
