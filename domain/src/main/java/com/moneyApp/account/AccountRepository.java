package com.moneyApp.account;

interface AccountRepository
{
    Account save(Account entity);

    void updateActualBalanceById(Double amount, Long accountId);

    Boolean existsByNameAndUserId(String name, Long userIdByEmail);

    Account update(Account account);
}
