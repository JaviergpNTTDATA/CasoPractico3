package com.casopractico3.CaPr3.unit;

import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    private static final String IBAN = "ES9121000418450200051332";
    private static final Long CLIENT_ID = 1L;

    @Test
    void shouldCreateAccountSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(CLIENT_ID);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban(IBAN);
        account.setBalance(BigDecimal.ZERO);

        when(clientRepository.findById(CLIENT_ID)).thenReturn(Optional.of(client));
        when(ibanGenerator.generateIban()).thenReturn(IBAN);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO result = accountService.createAccount(CLIENT_ID);

        assertNotNull(result);
        assertEquals(IBAN, result.getIban());
        assertEquals(CLIENT_ID, result.getClientId());
        verify(clientRepository, times(1)).findById(CLIENT_ID);
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundForCreate() {
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ClientNotFoundException.class, () -> accountService.createAccount(999L));
        verify(clientRepository, times(1)).findById(999L);
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldListClientAccountsSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(CLIENT_ID);

        Account account1 = new Account(client);
        account1.setId(1L);
        account1.setIban("ES9121000418450200051332");
        account1.setBalance(BigDecimal.valueOf(1000));

        Account account2 = new Account(client);
        account2.setId(2L);
        account2.setIban("ES9121000418450200051333");
        account2.setBalance(BigDecimal.valueOf(2000));

        when(clientRepository.existsById(CLIENT_ID)).thenReturn(true);
        when(accountRepository.findByClient_Id(CLIENT_ID)).thenReturn(Arrays.asList(account1, account2));

        List<AccountDTO> result = accountService.listClientAccounts(CLIENT_ID);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(clientRepository, times(1)).existsById(CLIENT_ID);
        verify(accountRepository, times(1)).findByClient_Id(CLIENT_ID);
    }

    @Test
    void shouldThrowExceptionWhenClientNotFoundForList() {
        when(clientRepository.existsById(999L)).thenReturn(false);

        assertThrows(ClientNotFoundException.class, () -> accountService.listClientAccounts(999L));
        verify(clientRepository, times(1)).existsById(999L);
        verify(accountRepository, never()).findByClient_Id(any());
    }

    @Test
    void shouldGetAccountByIbanSuccessfully() {
        Client client = new Client("Juan", "García", "12345678A", "123456789", "juan@example.com");
        client.setId(CLIENT_ID);

        Account account = new Account(client);
        account.setId(1L);
        account.setIban(IBAN);
        account.setBalance(BigDecimal.valueOf(1500));
        account.setCreatedAt(LocalDateTime.now());

        when(accountRepository.findByIban(IBAN)).thenReturn(Optional.of(account));

        AccountDTO result = accountService.getAccountByIban(IBAN);

        assertNotNull(result);
        assertEquals(IBAN, result.getIban());
        assertEquals(BigDecimal.valueOf(1500), result.getBalance());
        verify(accountRepository, times(1)).findByIban(IBAN);
    }

    @Test
    void shouldThrowExceptionWhenAccountNotFoundByIban() {
        when(accountRepository.findByIban("INVALID_IBAN")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class,
                () -> accountService.getAccountByIban("INVALID_IBAN"));
        verify(accountRepository, times(1)).findByIban("INVALID_IBAN");
    }
}
