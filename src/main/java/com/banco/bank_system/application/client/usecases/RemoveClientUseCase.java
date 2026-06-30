package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;
    private final AccountRepositoryPort accountRepository;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository,
                               AccountRepositoryPort accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    public void execute(CPF cpf){
        Client client =
                clientRepository.getClientByCpf(cpf)
                        .orElseThrow(ClientNotFoundException::new);

        removeAllClientAccounts(client.getId());

        clientRepository.delete(client.getId());
    }

    private void removeAllClientAccounts(UUID clientId){
        List<Account> accounts = accountRepository.getAccountsByClient(clientId);

        for (Account account : accounts) {
            if (!account.canBeRemoved()) {
                throw new CannotRemoveAccountException(
                        "Não é possível remover as contas. A conta " +
                                account.getAccountIdentity() + " possui saldo ativo."
                );
            }
        }

        accountRepository.removeClientAccounts(clientId);
    }
}
