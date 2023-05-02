package com.moneyApp.account.repository;

import com.moneyApp.account.Account;

import java.util.List;
import java.util.Optional;


public interface AccountRepository
{

    Optional<Account> findByNameAndUserId(String name, Long userId);

    Account save(Account entity);

    Boolean existsByNameAndUserId(String name, Long userIdByEmail);

    void updateActualBalanceById(Double amount, Long accountId);

    List<Account> findByUserId(Long userId);
}
