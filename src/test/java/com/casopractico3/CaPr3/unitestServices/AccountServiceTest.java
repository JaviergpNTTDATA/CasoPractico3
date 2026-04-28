package com.casopractico3.CaPr3.unitestServices;

import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.ClientRepository;
import com.casopractico3.CaPr3.repository.IbanGenerator;
import com.casopractico3.CaPr3.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private IbanGenerator ibanGenerator;

    @InjectMocks
    private AccountService accountService;

    private static final String IBAN = "ES91210000000000000001";

    @Test
    void shouldReturnAccountDtoByIban() {

        Client client = new Client();
        client.setId(1L);

        Account account = new Account(client);
        account.setIban(IBAN);
        account.setBalance(BigDecimal.valueOf(1000));
        account.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByIban(IBAN))
                .thenReturn(Optional.of(account));

        AccountDTO result = accountService.getAccountByIban(IBAN);

        assertEquals(IBAN, result.getIban());
        assertEquals(1L, result.getClientId());
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByIban() {

        when(accountRepository.findByIban(IBAN))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountByIban(IBAN));
    }
}
