package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;
    private final AccountRepositoryPort accountRepository;

    private final ClientFinder clientFinder;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository,
                               AccountRepositoryPort accountRepository,
                               ClientFinder clientFinder){
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
        this.clientFinder = clientFinder;
    }

    @Transactional
    public void execute(CPF cpf) {

        Client client = clientFinder.find(cpf);

        validateAccounts(client.getId());

        accountRepository.removeClientAccounts(client.getId());

        clientRepository.delete(client.getId());
    }

    private void validateAccounts(ClientId clientId) {

        boolean hasNonRemovableAccount =
                accountRepository
                        .getAccountsByClient(clientId)
                        .stream()
                        .anyMatch(account -> !account.isRemovable());

        if (hasNonRemovableAccount) {
            throw new CannotRemoveAccountException(
                    "Não foi possível remover as contas. Cliente possui conta com saldo ativo"
            );
        }
    }
}
