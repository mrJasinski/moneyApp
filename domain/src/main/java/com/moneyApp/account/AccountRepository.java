package com.moneyApp.account;

interface AccountRepository
{
    AccountSnapshot save(AccountSnapshot entity);

    void updateActualBalanceById(Double amount, Long accountId);

    void deleteByNameAndUserId(String name, Long userId);
}
