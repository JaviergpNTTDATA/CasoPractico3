package com.casopractico3.CaPr3.controller;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Movements", description = "Operations related to account movements")
@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class OperationController {

    private final OperationService service;

    @Operation(
            summary = "Deposit money",
            description = "Deposits a specified amount into an account"
    )
    @ApiResponse(responseCode = "200", description = "Deposit completed successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/deposit")
    public MovementDTO deposit(@RequestParam String iban,
                               @RequestParam BigDecimal amount) {

        return service.deposit(iban.toUpperCase(), amount);
    }

    @Operation(
            summary = "Withdraw money",
            description = "Withdraws a specified amount from an account"
    )
    @ApiResponse(responseCode = "200", description = "Withdrawal completed successfully")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @PostMapping("/withdraw")
    public MovementDTO withdraw(@RequestParam String iban,
                                @RequestParam BigDecimal amount) {

        return service.withdraw(iban.toUpperCase(), amount);
    }

    @Operation(
            summary = "Transfer money between accounts",
            description = "Transfers a specified amount from one account to another"
    )
    @ApiResponse(responseCode = "200", description = "Transfer completed successfully")
    @ApiResponse(responseCode = "404", description = "One or both accounts not found")
    @PostMapping("/transfer")
    public List<MovementDTO> transfer(@RequestParam String sourceIban, @RequestParam String targetIban, @RequestParam BigDecimal amount) {

        return service.transfer(
                sourceIban.toUpperCase(),
                targetIban.toUpperCase(),
                amount
        );
    }
}
