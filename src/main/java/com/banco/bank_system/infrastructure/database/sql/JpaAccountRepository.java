package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaAccountRepository
        extends JpaRepository<AccountEntity, UUID> {


    Optional<AccountEntity> findByAccountNumberAndBranch(
            String accountNumber,
            String branch
    );


    List<AccountEntity> findByClientId(UUID clientId);


    boolean existsByAccountNumberAndBranch(
            String accountNumber,
            String branch
    );


    void deleteByClientId(UUID clientId);

}