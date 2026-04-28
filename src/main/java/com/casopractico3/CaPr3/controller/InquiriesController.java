package com.casopractico3.CaPr3.controller;

import com.casopractico3.CaPr3.dto.AccountBalanceDTO;
import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.service.InquiriesService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Inquiries", description = "Operations related to inquiries")
@RestController
@RequestMapping("/inquiries")
public class InquiriesController {

    private final InquiriesService inquiriesService;

    public InquiriesController(InquiriesService inquiriesService) {
        this.inquiriesService = inquiriesService;
    }

    @ApiResponse(responseCode = "404", description = "No movements found")
    @GetMapping("/account/{iban}")
    public List<MovementDTO> getByAccount(@PathVariable String iban) {
        return inquiriesService.getByAccount(iban);
    }

    @ApiResponse(responseCode = "404", description = "No movements found")
    @GetMapping("/account/{iban}/between")
    public List<MovementDTO> getByAccountAndDates(
            @PathVariable String iban,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return inquiriesService.getByAccountAndDates(iban, start, end);
    }
    @ApiResponse(responseCode = "200", description = "Account found")
    @ApiResponse(responseCode = "404", description = "Account not found")
    @GetMapping("/account/{iban}/balance")
    public AccountBalanceDTO getBalance(@PathVariable String iban) {
        return inquiriesService.getBalance(iban);
    }

}
