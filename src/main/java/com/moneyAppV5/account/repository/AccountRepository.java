package com.moneyAppV5.account.repository;

import com.moneyAppV5.account.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository
{
    List<Account> findAll();

    Optional<Account> findById(Integer id);
    Optional<Account> findByHash(Integer hash);
    Account findByName(String name);
    Account save(Account entity);
    boolean existsById(int id);
    boolean existsByName(String name);

    double getAccountActualBalance(Account account);

    void changeBalance(Integer id, double amount);

    Optional<Double> sumAllAccountsBalances();
}
