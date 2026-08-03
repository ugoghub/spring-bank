package com.banco.bank_system.useCase.accountUseCaseTests.util;

import com.banco.bank_system.application.account.port.AccountRepositoryPort;
import com.banco.bank_system.application.account.util.UniqueAccountIdentityGenerator;
import com.banco.bank_system.domain.valueobject.AccountIdentity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UniqueAccountIdentityGeneratorTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private UniqueAccountIdentityGenerator uniqueAccountIdentityGenerator;

    @Test
    void shouldGenerateUniqueAccountIdentity() {

        when(accountRepository.existsByAccountIdentity(any()))
                .thenReturn(false);

        AccountIdentity identity = uniqueAccountIdentityGenerator.generate();

        assertNotNull(identity);

        verify(accountRepository)
                .existsByAccountIdentity(identity);
    }

    @Test
    void shouldRetryGenerationUntilIdentityIsUnique() {

        when(accountRepository.existsByAccountIdentity(any()))
                .thenReturn(true)
                .thenReturn(false);

        AccountIdentity identity = uniqueAccountIdentityGenerator.generate();

        assertNotNull(identity);

        verify(accountRepository, times(2))
                .existsByAccountIdentity(any());
    }
}
