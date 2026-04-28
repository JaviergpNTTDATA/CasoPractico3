package com.casopractico3.CaPr3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MovementDTO {
    private String iban;
    private String type;
    private BigDecimal amount;
    private LocalDateTime createdAt;

    public MovementDTO() {

    }
}
