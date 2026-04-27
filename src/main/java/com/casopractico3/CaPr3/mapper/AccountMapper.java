package com.casopractico3.CaPr3.mapper;

import com.casopractico3.CaPr3.dto.AccountDTO;
import com.casopractico3.CaPr3.model.Account;

public class AccountMapper {

    public static AccountDTO toDTO(Account account) {
        return new AccountDTO(
                account.getIban(),
                account.getBalance(),
                account.getCreatedAt(),
                account.getClient().getId()
        );
    }
}
