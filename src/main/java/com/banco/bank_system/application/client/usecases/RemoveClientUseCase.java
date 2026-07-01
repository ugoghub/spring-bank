package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.valueobject.ClientId;
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

        validateAccounts(client.getId());
        removeAllClientAccounts(client.getId());

        clientRepository.delete(client.getId());
    }

    private void validateAccounts(ClientId clientId){
        List<Account> accounts = accountRepository.getAccountsByClient(clientId);

        for (Account account : accounts) {
            if (!account.isRemovable()) {
                throw new CannotRemoveAccountException(
                        "Não foi possível remover as contas. Cliente possui conta com saldo ativo"
                );
            }
        }

        accountRepository.removeClientAccounts(clientId);
    }

    private void removeAllClientAccounts(ClientId clientId){
        accountRepository.removeClientAccounts(clientId);
    }
}
