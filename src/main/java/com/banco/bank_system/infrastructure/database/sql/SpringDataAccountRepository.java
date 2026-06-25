package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.infrastructure.database.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, UUID> {
}
