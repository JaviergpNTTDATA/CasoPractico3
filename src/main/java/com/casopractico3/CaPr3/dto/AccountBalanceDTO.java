package com.casopractico3.CaPr3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AccountBalanceDTO {

    private String iban;
    private BigDecimal balance;
}