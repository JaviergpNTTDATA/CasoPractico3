package com.casopractico3.CaPr3.controllers;

import com.casopractico3.CaPr3.controller.AccountController;
import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.security.JwtFilter;
import com.casopractico3.CaPr3.service.AccountService;
import com.casopractico3.CaPr3.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtFilter jwtFilter;

    @Test
    void shouldReturnAccountsByClient() throws Exception {

        when(accountService.listClientAccounts(1L))
                .thenReturn(List.of());

        mockMvc.perform(get("/accounts/client/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenAccountNotFound() throws Exception {

        when(accountService.getAccountByIban("ES404"))
                .thenThrow(new RuntimeException());

        mockMvc.perform(get("/accounts/ES404"))
                .andExpect(status().isNotFound());
    }
}
