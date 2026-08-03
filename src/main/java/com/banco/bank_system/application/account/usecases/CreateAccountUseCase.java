package com.banco.bank_system.application.account.usecases;

import com.banco.bank_system.application.account.dto.CreateAccountOutput;
import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.UniqueAccountIdentityGenerator;
import com.banco.bank_system.application.client.util.ClientFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.CheckingAccount;
import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.domain.entities.SavingsAccount;
import com.banco.bank_system.domain.enums.AccountType;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.CPF;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class CreateAccountUseCase {

    private final AccountRepositoryPort accountRepository;
    private final ClientFinder clientFinder;

    private final UniqueAccountIdentityGenerator uniqueAccountIdentityGenerator;

    private final Clock clock;


    public CreateAccountUseCase(AccountRepositoryPort accountRepository,
                                ClientFinder clientFinder,
                                UniqueAccountIdentityGenerator uniqueAccountIdentityGenerator,
                                Clock clock) {
        this.accountRepository = accountRepository;
        this.clientFinder = clientFinder;
        this.uniqueAccountIdentityGenerator = uniqueAccountIdentityGenerator;
        this.clock = clock;
    }

    public CreateAccountOutput execute(CPF cpf, AccountType type){

        Client client = clientFinder.find(cpf);

        AccountIdentity accountIdentity = uniqueAccountIdentityGenerator.generate();

        final Account account =
                switch (type) {
                    case CHECKING ->
                            CheckingAccount.create(
                                    client.getId(),
                                    accountIdentity,
                                    clock
                            );

                    case SAVINGS ->
                            SavingsAccount.create(
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
