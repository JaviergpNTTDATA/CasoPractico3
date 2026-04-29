package com.casopractico3.CaPr3.controller;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.model.OperationType;
import com.casopractico3.CaPr3.service.OperationService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Disabled("Diaasables until i can fix the ApplicationContext")
@WebMvcTest(OperationController.class)
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OperationService operationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldDepositSuccessfully() throws Exception {
        MovementDTO movementDTO = MovementDTO.builder()
                .type("DEPOSIT")
                .amount(BigDecimal.valueOf(500))
                .createdAt(LocalDateTime.now())
                .build();

        Map<String, Object> request = new HashMap<>();
        request.put("iban", "ES9121000418450200051332");
        request.put("amount", 500);

        when(operationService.deposit("ES9121000418450200051332", BigDecimal.valueOf(500)))
                .thenReturn(movementDTO);

        mockMvc.perform(post("/operations/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.amount").value(500));
    }

    @Test
    void shouldReturn404WhenAccountNotFoundForDeposit() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("iban", "INVALID_IBAN");
        request.put("amount", 500);

        when(operationService.deposit("INVALID_IBAN", BigDecimal.valueOf(500)))
                .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(post("/operations/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldWithdrawSuccessfully() throws Exception {
        MovementDTO movementDTO = MovementDTO.builder()
                .iban("ES9121000418450200051332")
                .type("WITHDRAW")
                .amount(BigDecimal.valueOf(200))
                .createdAt(LocalDateTime.now())
                .build();

        Map<String, Object> request = new HashMap<>();
        request.put("iban", "ES9121000418450200051332");
        request.put("amount", 200);

        when(operationService.withdraw("ES9121000418450200051332", BigDecimal.valueOf(200)))
                .thenReturn(movementDTO);

        mockMvc.perform(post("/operations/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("WITHDRAW"))
                .andExpect(jsonPath("$.amount").value(200));
    }

    @Test
    void shouldReturn404WhenAccountNotFoundForWithdraw() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("iban", "INVALID_IBAN");
        request.put("amount", 200);

        when(operationService.withdraw("INVALID_IBAN", BigDecimal.valueOf(200)))
                .thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(post("/operations/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldTransferSuccessfully() throws Exception {
        MovementDTO debit = MovementDTO.builder()
                .iban("ES9121000418450200051332")
                .type("OUTGOING_TRANSFER")
                .amount(BigDecimal.valueOf(500))
                .createdAt(LocalDateTime.now())
                .build();

        MovementDTO credit = MovementDTO.builder()
                .iban("ES9121000418450200051333")
                .type("INCOMING_TRANSFER")
                .amount(BigDecimal.valueOf(500))
                .createdAt(LocalDateTime.now())
                .build();

        Map<String, Object> request = new HashMap<>();
        request.put("sourceIban", "ES9121000418450200051332");
        request.put("targetIban", "ES9121000418450200051333");
        request.put("amount", 500);

        when(operationService.transfer("ES9121000418450200051332", "ES9121000418450200051333",
                BigDecimal.valueOf(500))).thenReturn(Arrays.asList(debit, credit));

        mockMvc.perform(post("/operations/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type").value("OUTGOING_TRANSFER"))
                .andExpect(jsonPath("$[1].type").value("INCOMING_TRANSFER"));
    }

    @Test
    void shouldReturn404WhenSourceAccountNotFoundForTransfer() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("sourceIban", "INVALID_IBAN");
        request.put("targetIban", "ES9121000418450200051333");
        request.put("amount", 500);

        when(operationService.transfer("INVALID_IBAN", "ES9121000418450200051333",
                BigDecimal.valueOf(500))).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(post("/operations/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidAmount() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("iban", "ES9121000418450200051332");
        request.put("amount", -100);

        mockMvc.perform(post("/operations/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
