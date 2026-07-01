package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class DepositUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;
    private final AccountFinder accountFinder;

    private final Clock clock;

    public DepositUseCase(TransactionRepositoryPort transactionRepository,
                          AccountRepositoryPort accountRepository,
                          AccountFinder accountFinder,
                          Clock clock) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
        this.accountFinder = accountFinder;
        this.clock = clock;
    }

    @Transactional
    public DepositOutput execute(AccountIdentity accountIdentity, Money value){

        Account account = accountFinder.byIdentity(accountIdentity);

        account.deposit(value);

        accountRepository.save(account);

        Transaction deposit = Transaction.deposit(
                account.getId(),
                value,
                clock
        );

        transactionRepository.save(deposit);

        return new DepositOutput(
                account.getId(),
                deposit.getAmount(),
                account.getBalance(),
                deposit.getId(),
                deposit.getDateTime()
        );
    }
}
