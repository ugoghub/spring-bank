package com.banco.bank_system.infrastructure.database.sql;

import com.banco.bank_system.infrastructure.database.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SpringDataClientRepository
        extends JpaRepository<ClientEntity, UUID> {


    Optional<ClientEntity> findByCpf(String cpf);


    boolean existsByCpf(String cpf);


    boolean existsByEmail(String email);

}