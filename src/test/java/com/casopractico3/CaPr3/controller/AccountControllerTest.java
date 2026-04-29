package com.casopractico3.CaPr3.controller;

import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccountSuccessfully() throws Exception {
        AccountDTO accountDTO = new AccountDTO("ES9121000418450200051332", BigDecimal.ZERO,
                LocalDateTime.now(), 1L);

        when(accountService.createAccount(1L)).thenReturn(accountDTO);

        mockMvc.perform(post("/accounts/create/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("ES9121000418450200051332"))
                .andExpect(jsonPath("$.clientId").value(1));

        verify(accountService, times(1)).createAccount(1L);
    }

    @Test
    void shouldReturn404WhenClientNotFoundForCreate() throws Exception {
        when(accountService.createAccount(999L))
                .thenThrow(new ClientNotFoundException("Client not found: 999"));

        mockMvc.perform(post("/accounts/create/999"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).createAccount(999L);
    }

    @Test
    void shouldListClientAccountsSuccessfully() throws Exception {
        List<AccountDTO> accounts = Arrays.asList(
                new AccountDTO("ES9121000418450200051332", BigDecimal.valueOf(1000),
                        LocalDateTime.now(), 1L),
                new AccountDTO("ES9121000418450200051333", BigDecimal.valueOf(2000),
                        LocalDateTime.now(), 1L)
        );

        when(accountService.listClientAccounts(1L)).thenReturn(accounts);

        mockMvc.perform(get("/accounts/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].iban").value("ES9121000418450200051332"))
                .andExpect(jsonPath("$[1].iban").value("ES9121000418450200051333"));

        verify(accountService, times(1)).listClientAccounts(1L);
    }

    @Test
    void shouldReturn404WhenClientNotFoundForList() throws Exception {
        when(accountService.listClientAccounts(999L))
                .thenThrow(new ClientNotFoundException("Client not found: 999"));

        mockMvc.perform(get("/accounts/client/999"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).listClientAccounts(999L);
    }

    @Test
    void shouldReturnEmptyListWhenClientHasNoAccounts() throws Exception {
        when(accountService.listClientAccounts(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/accounts/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        verify(accountService, times(1)).listClientAccounts(1L);
    }

    @Test
    void shouldGetAccountByIbanSuccessfully() throws Exception {
        AccountDTO accountDTO = new AccountDTO("ES9121000418450200051332", BigDecimal.valueOf(1500),
                LocalDateTime.now(), 1L);

        when(accountService.getAccountByIban("ES9121000418450200051332")).thenReturn(accountDTO);

        mockMvc.perform(get("/accounts/iban/ES9121000418450200051332"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("ES9121000418450200051332"))
                .andExpect(jsonPath("$.balance").value(1500));

        verify(accountService, times(1)).getAccountByIban("ES9121000418450200051332");
    }

    @Test
    void shouldReturn404WhenAccountNotFoundByIban() throws Exception {
        when(accountService.getAccountByIban("INVALID_IBAN"))
                .thenThrow(new AccountNotFoundException("Account not found with IBAN: INVALID_IBAN"));

        mockMvc.perform(get("/accounts/iban/INVALID_IBAN"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getAccountByIban("INVALID_IBAN");
    }

    @Test
    void shouldConvertIbanToUpperCase() throws Exception {
        AccountDTO accountDTO = new AccountDTO("ES9121000418450200051332", BigDecimal.valueOf(1000),
                LocalDateTime.now(), 1L);

        when(accountService.getAccountByIban("ES9121000418450200051332")).thenReturn(accountDTO);

        mockMvc.perform(get("/accounts/iban/es9121000418450200051332"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("ES9121000418450200051332"));

        verify(accountService, times(1)).getAccountByIban("ES9121000418450200051332");
    }
}
