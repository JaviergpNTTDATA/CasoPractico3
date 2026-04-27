package com.casopractico3.CaPr3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class AccountDTO {

    private String iban;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private Long clientId;
}
