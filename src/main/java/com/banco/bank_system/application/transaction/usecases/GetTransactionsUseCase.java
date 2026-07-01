package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.exception.AccountNotFoundException;
import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.List;

@Service
public class GetTransactionsUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountFinder accountFinder;
    ;

    public GetTransactionsUseCase(TransactionRepositoryPort transactionRepository,
                          AccountFinder accountFinder) {
        this.transactionRepository = transactionRepository;
        this.accountFinder = accountFinder;
    }

    public List<TransactionDTO> execute(AccountIdentity accountIdentity){

        Account account = accountFinder.byIdentity(accountIdentity);

        List<Transaction> transactions = transactionRepository.findByAccountId(account.getId());

        return TransactionDTO.from(transactions);
    }
}
