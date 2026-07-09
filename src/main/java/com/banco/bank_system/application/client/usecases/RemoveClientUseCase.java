package com.banco.bank_system.application.client.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.application.exception.CannotRemoveAccountException;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.domain.valueobject.ClientId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RemoveClientUseCase {

    private final ClientRepositoryPort clientRepository;
    private final AccountRepositoryPort accountRepository;

    public RemoveClientUseCase(ClientRepositoryPort clientRepository,
                               AccountRepositoryPort accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void execute(CPF cpf) {
        Client client =
                clientRepository.getClientByCpf(cpf)
                        .orElseThrow(ClientNotFoundException::new);

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
