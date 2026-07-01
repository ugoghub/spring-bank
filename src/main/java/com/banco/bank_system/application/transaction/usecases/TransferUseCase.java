package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.InvalidTransferException;
import com.banco.bank_system.application.transaction.dto.TransferOutput;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import com.banco.bank_system.domain.valueobject.Money;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.UUID;

@Service
public class TransferUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountRepositoryPort accountRepository;

    private final AccountFinder accountFinder;

    private final Clock clock;

    public TransferUseCase(TransactionRepositoryPort transactionRepository,
                          AccountRepositoryPort accountRepository,
                          AccountFinder accountFinder,
                          Clock clock) {
        this.transactionRepository = transactionRepository;
        this.accountRepository =accountRepository;
        this.accountFinder = accountFinder;
        this.clock = clock;
    }

    @Transactional
    public TransferOutput execute(
            AccountIdentity fromAccountIdentity,
            AccountIdentity toAccountIdentity,
            Money value
    ){
        Account from = accountFinder.byIdentity(fromAccountIdentity);

        Account to = accountFinder.byIdentity(toAccountIdentity);

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
