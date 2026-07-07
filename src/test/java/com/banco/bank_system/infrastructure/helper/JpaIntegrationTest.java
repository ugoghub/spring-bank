package com.banco.bank_system.infrastructure.helper;

import com.banco.bank_system.infrastructure.database.sql.JpaAccountRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaClientRepository;
import com.banco.bank_system.infrastructure.database.sql.JpaTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@DataJpaTest
@ActiveProfiles("test")
abstract class JpaIntegrationTest {

    @Autowired
    protected JpaClientRepository clientRepository;

    @Autowired
    protected JpaAccountRepository accountRepository;

    @Autowired
    protected JpaTransactionRepository transactionRepository;

    protected Clock clock;

    @BeforeEach
    void setupClock() {
        clock = Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );
    }
}
