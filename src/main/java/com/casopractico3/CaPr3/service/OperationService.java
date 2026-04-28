package com.casopractico3.CaPr3.service;

import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.mapper.MovementMapper;
import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.model.OperationType;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OperationService {

    private final AccountRepository accountRepository;
    private final MovementRepository movementRepository;

    public OperationService(AccountRepository accountRepository,
                            MovementRepository movementRepository) {
        this.accountRepository = accountRepository;
        this.movementRepository = movementRepository;
    }

    public MovementDTO deposit(String iban, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with IBAN: " + iban));

        account.deposit(amount);

        Movement movement = Movement.builder()
                .account(account)
                .type(OperationType.DEPOSIT)
                .amount(amount)
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }

    public MovementDTO withdraw(String iban, BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with IBAN: " + iban));

        account.withdraw(amount);

        Movement movement = Movement.builder()
                .account(account)
                .type(OperationType.WITHDRAW)
                .amount(amount)
                .build();

        Movement saved = movementRepository.save(movement);

        return MovementMapper.toDto(saved);
    }
    @Transactional
    public List<MovementDTO> transfer(String sourceIban, String targetIban, BigDecimal amount) {

        if (sourceIban.equalsIgnoreCase(targetIban)) {
            throw new IllegalArgumentException("Source and target accounts must be different");
        }

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        Account source = accountRepository.findByIban(sourceIban)
                .orElseThrow(() ->
                        new AccountNotFoundException("Source account not found: " + sourceIban));

        Account target = accountRepository.findByIban(targetIban)
                .orElseThrow(() ->
                        new AccountNotFoundException("Target account not found: " + targetIban));

        if (source.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        source.withdraw(amount);
        target.deposit(amount);

        Movement debit = Movement.builder()
                .account(source)
                .type(OperationType.OUTGOING_TRANSFER)
                .amount(amount)
                .build();

        Movement credit = Movement.builder()
                .account(target)
                .type(OperationType.INCOMING_TRANSFER)
                .amount(amount)
                .build();

        movementRepository.save(debit);
        movementRepository.save(credit);

        return List.of(
                MovementMapper.toDto(debit),
                MovementMapper.toDto(credit)
        );
    }

}
