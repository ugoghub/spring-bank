package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import com.banco.bank_system.application.exception.InvalidTransferException;
import com.banco.bank_system.application.exception.ClientNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class TransferUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    public TransferUseCase(TransactionRepositoryPort transactionRepository, AccountRepositoryPort accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
    }

    @Transactional
    public TransferOutput execute(
            AccountIdentity fromAccountIdentity,
            AccountIdentity toAccountIdentity,
            Money value,
            Clock clock
    ){
        Account from = accountRepository
                .getAccountByAccountIdentity(fromAccountIdentity)
                .orElseThrow(AccountNotFoundException::new);

        Account to = accountRepository
                .getAccountByAccountIdentity(toAccountIdentity)
                .orElseThrow(AccountNotFoundException::new);

        if (from.equals(to)) {
            throw new InvalidTransferException("Não é possível transferir para a mesma conta");
        }

        from.withdraw(value);

        to.deposit(value);

        accountRepository.save(from);
        accountRepository.save(to);

        UUID operationId = UUID.randomUUID();

        Transaction fromTransaction = Transaction.transferSent(
                operationId,
                from.getId(),
                to.getId(),
                value,
                clock
        );

        Transaction toTransaction = Transaction.transferReceived(
                operationId,
                from.getId(),
                to.getId(),
                value,
                clock
        );

        transactionRepository.save(
                fromTransaction
        );

        transactionRepository.save(
                toTransaction
        );

        return new TransferOutput(
                operationId,
                from.getId(),
                to.getId(),
                fromTransaction.getAmount(),
                fromTransaction.getDateTime()
        );
    }
}
