package com.casopractico3.CaPr3.service;

import com.casopractico3.CaPr3.dto.AccountBalanceDTO;
import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.dto.MovementDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.exception.MovementNotFoundException;
import com.casopractico3.CaPr3.mapper.MovementMapper;
import com.casopractico3.CaPr3.model.Movement;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.MovementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InquiriesService {

    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper;
    private final AccountRepository accountRepository;

    public InquiriesService(MovementRepository movementRepository, MovementMapper movementMapper, AccountRepository accountRepository) {
        this.movementRepository = movementRepository;
        this.movementMapper = movementMapper;
        this.accountRepository = accountRepository;
    }


    public List<MovementDTO> getByAccount(String iban) {

        accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException(iban));

        List<Movement> movements =
                movementRepository.findByAccount_ibanOrderByCreatedAtDesc(iban);

        if (movements.isEmpty()) {
            throw new MovementNotFoundException(
                    "No movements found for account: " + iban);
        }

        return movements.stream()
                .map(movementMapper::toDtoN)
                .collect(Collectors.toList());
    }


    public List<MovementDTO> getByAccountAndDates(String iban,
                                                  LocalDate start,
                                                  LocalDate end) {

        accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException(iban));

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        List<Movement> movements =
                movementRepository
                        .findByAccount_ibanAndCreatedAtBetweenOrderByCreatedAtDesc(
                                iban, startDateTime, endDateTime);

        if (movements.isEmpty()) {
            throw new MovementNotFoundException(
                    "No movements found for account: "
                            + iban + " between " + start + " and " + end);
        }

        return movements.stream()
                .map(movementMapper::toDtoN)
                .toList();
    }
    @Transactional(readOnly = true)
    public AccountBalanceDTO getBalance(String iban) {

        var account = accountRepository.findByIban(iban)
                .orElseThrow(() -> new AccountNotFoundException(iban));

        return new AccountBalanceDTO(account.getIban(),account.getBalance());
    }

}
