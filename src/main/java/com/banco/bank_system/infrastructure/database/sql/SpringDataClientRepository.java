package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.domain.entities.Client;
import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataClientRepository extends JpaRepository<ClientEntity, UUID> {
}
