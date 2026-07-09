package com.banco.bank_system.useCase.accountUseCaseTests;

import com.banco.bank_system.application.account.dto.GetClientAccountOutput;
import com.banco.bank_system.application.account.usecases.GetAccountUseCase;
import com.banco.bank_system.application.account.util.AccountFinder;
import com.banco.bank_system.domain.entities.Account;
import com.banco.bank_system.entities.helper.AccountFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetClientAccountUseCaseTest {

    @Mock
    private AccountFinder accountFinder;

    @InjectMocks
    private GetAccountUseCase useCase;

    @Test
    void shouldReturnClientAccount() {

        Account account = AccountFactory.checking(Clock.systemUTC());

        when(accountFinder.byIdentity(account.getAccountIdentity()))
                .thenReturn(account);

        GetClientAccountOutput output = useCase.execute(account.getAccountIdentity());

        assertEquals(account.getId(), output.id());

        verify(accountFinder)
                .byIdentity(account.getAccountIdentity());

        verifyNoMoreInteractions(accountFinder);
    }
}
