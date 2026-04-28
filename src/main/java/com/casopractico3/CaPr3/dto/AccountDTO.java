package com.casopractico3.CaPr3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class AccountDTO {

    private String iban;
    private BigDecimal balance;
    private LocalDateTime createdAt;
    private Long clientId;

    public AccountDTO() {

    }
}
