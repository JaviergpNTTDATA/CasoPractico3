package com.casopractico3.CaPr3.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MovementDTO {
    private String iban;
    private String type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
}
