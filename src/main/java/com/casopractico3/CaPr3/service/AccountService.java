package com.casopractico3.CaPr3.service;

import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.exception.AccountNotFoundException;
import com.casopractico3.CaPr3.exception.ClientNotFoundException;
import com.casopractico3.CaPr3.mapper.AccountMapper;
import com.casopractico3.CaPr3.model.Account;
import com.casopractico3.CaPr3.model.Client;
import com.casopractico3.CaPr3.repository.AccountRepository;
import com.casopractico3.CaPr3.repository.ClientRepository;
import com.casopractico3.CaPr3.repository.IbanGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientRepository clientRepository;
    private final IbanGenerator ibanGenerator;

    public AccountService(AccountRepository accountRepository,
                          ClientRepository clientRepository,
                          IbanGenerator ibanGenerator) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
        this.ibanGenerator = ibanGenerator;
    }

    public AccountDTO createAccount(Long clientId) {

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() ->
                        new ClientNotFoundException("Client not found: " + clientId));

        Account account = new Account(client);
        account.setIban(ibanGenerator.generateIban());

        Account saved = accountRepository.save(account);

        return AccountMapper.toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<AccountDTO> listClientAccounts(Long clientId) {

        if (!clientRepository.existsById(clientId)) {
            throw new ClientNotFoundException("Client not found: " + clientId);
        }

        return accountRepository.findByClient_Id(clientId)
                .stream()
                .map(AccountMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AccountDTO getAccountByIban(String iban) {

        Account account = accountRepository.findByIban(iban)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account not found with IBAN: " + iban));

        return AccountMapper.toDTO(account);
    }
}
