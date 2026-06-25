package com.banco.bank_system.application.transfer.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.transfer.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;

import java.time.Clock;
import java.util.UUID;

public class TransferUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    public TransferUseCase(TransactionRepositoryPort transactionRepository, AccountRepositoryPort accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
    }

    public void execute(
            AccountIdentity fromAccountIdentity,
            AccountIdentity toAccountIdentity,
            Money value,
            Clock clock
    ){
        Account from = accountRepository
                .getAccountByAccountIdentity(fromAccountIdentity)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));
        Account to = accountRepository
                .getAccountByAccountIdentity(toAccountIdentity)
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada"));

        if (from.equals(to)) {
            throw new IllegalArgumentException("Não é possível transferir para a mesma conta");
        }

        from.withdraw(value);

        to.deposit(value);

        UUID operationId = UUID.randomUUID();

        transactionRepository.save(
                from.getId(),
                Transaction.transferSent(
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        value,
                        clock
                )
        );

        transactionRepository.save(
                to.getId(),
                Transaction.transferReceived(
                        operationId,
                        from.getAccountIdentity(),
                        to.getAccountIdentity(),
                        value,
                        clock
                )
        );
    }
}
