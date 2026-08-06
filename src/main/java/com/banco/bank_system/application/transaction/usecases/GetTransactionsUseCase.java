package com.banco.bank_system.application.transaction.usecases;

import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.application.transaction.dto.TransactionDTO;
import com.banco.bank_system.application.transaction.port.TransactionRepositoryPort;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.domain.entities.Transaction;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetTransactionsUseCase {

    private final TransactionRepositoryPort transactionRepository;
    private final AccountFinder accountFinder;


    public GetTransactionsUseCase(TransactionRepositoryPort transactionRepository,
                          AccountFinder accountFinder) {
        this.transactionRepository = transactionRepository;
        this.accountFinder = accountFinder;
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> execute(
            AccountIdentity accountIdentity,
            int page,
            int size
    ){

        Account account = accountFinder.byIdentity(accountIdentity);

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt").descending()
                );

        Page<Transaction> transactions =
                transactionRepository.findByAccountId(
                        account.getId(),
                        pageable
                );

        return transactions.map(TransactionDTO::from);
    }
}
