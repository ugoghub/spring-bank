package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.transaction.dto.DepositOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class DepositUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    public DepositUseCase(TransactionRepositoryPort transactionRepository, AccountRepositoryPort accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
    }

    @Transactional
    public DepositOutput execute(AccountIdentity accountIdentity, Money value, Clock clock){
        Account account = accountRepository
                .getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(ClientNotFoundException::new);

        account.deposit(value);

        accountRepository.save(account);

        Transaction deposit = Transaction.deposit(
                account.getAccountIdentity(),
                value,
                clock
        );

        transactionRepository.save(
                account.getId(),
                deposit);

        return new DepositOutput(
                account.getAccountIdentity(),
                deposit.getAmount(),
                account.getBalance(),
                deposit.getId(),
                deposit.getDateTime()
        );
    }
}
