package com.banco.bank_system.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

@TestConfiguration
public class FixedClockTestConfiguration {

    @Bean
    Clock clock() {
        return Clock.fixed(
                Instant.parse("2026-01-10T10:00:00Z"),
                ZoneOffset.UTC
        );
    }
}
