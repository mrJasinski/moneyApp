package com.moneyApp.account;

import com.moneyApp.account.Account;

import java.util.List;
import java.util.Optional;


public interface AccountRepository
{
    Optional<Account> findByNameAndUserId(String name, Long userId);
    Optional<Account> findById(Long accountId);

    Account save(Account entity);

    Boolean existsByNameAndUserId(String name, Long userIdByEmail);

    void updateActualBalanceById(Double amount, Long accountId);

    List<Account> findByUserId(Long userId);
}
