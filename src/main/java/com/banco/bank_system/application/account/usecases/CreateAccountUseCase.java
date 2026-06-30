package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.client.port.ClientRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.AccountIdentityFactory;
import com.banco.bank_system.domain.valueobject.CPF;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final ClientRepositoryPort clientRepository;
    private final Clock clock;


    public CreateAccountUseCase(AccountRepositoryPort accountRepository,
                                ClientRepositoryPort clientRepository,
                                Clock clock) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.clock = clock;
    }

    public CreateAccountOutput execute(CPF cpf, AccountType type){

        Client client = clientRepository.getClientByCpf(cpf)
                .orElseThrow(ClientNotFoundException::new);

        AccountIdentity accountIdentity;

        do {

            accountIdentity = AccountIdentityFactory.generate();

        } while (accountRepository.existsByAccountIdentity(accountIdentity)); // garante unicidade do AccountIdentity

        final Account account =
                switch (type) {
                    case CHECKING ->
                            new CheckingAccount(
                                    client.getId(),
                                    accountIdentity,
                                    clock
                            );

                    case SAVINGS ->
                            new SavingsAccount(
                                    client.getId(),
                                    accountIdentity,
                                    clock
                            );
                };

        accountRepository.save(account);

        return new CreateAccountOutput(
                account.getId(),
                account.getClientId(),
                account.getAccountIdentity(),
                account.getCreationTime(),
                account.getBalance()
        );
    }
}
