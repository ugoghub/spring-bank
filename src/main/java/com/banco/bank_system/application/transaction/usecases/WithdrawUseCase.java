package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.transaction.dto.WithdrawOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import jakarta.transaction.Transactional;

import java.time.Clock;

public class WithdrawUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    public WithdrawUseCase(TransactionRepositoryPort transactionRepository, AccountRepositoryPort accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
    }

    @Transactional
    public WithdrawOutput execute(AccountIdentity accountIdentity, Money value, Clock clock){
        Account account = accountRepository
                .getAccountByAccountIdentity(accountIdentity)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        account.withdraw(value);

        accountRepository.save(account);

        Transaction withdraw = Transaction.withdraw(
                account.getAccountIdentity(),
                value,
                clock
        );

        transactionRepository.save(
                account.getId(),
                withdraw
        );

        return new WithdrawOutput(
                account.getAccountIdentity(),
                withdraw.getAmount(),
                account.getBalance(),
                withdraw.getId(),
                withdraw.getDateTime()
        );
    }
}
