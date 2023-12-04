package com.moneyApp.account;

interface AccountRepository
{
    AccountSnapshot save(AccountSnapshot entity);

    void updateActualBalanceById(Double amount, Long accountId);

//    Account update(Account account);
}
