package com.moneyApp.account;

import com.moneyApp.vo.BillSource;
import com.moneyApp.vo.UserSource;

import java.util.Set;

class Account
{
    static Account restore(AccountSnapshot snapshot)
    {
        return new Account(
                snapshot.getId()
                , snapshot.getName()
                , snapshot.getDescription()
                , snapshot.getActualBalance()
//                , snapshot.getBills()
                , snapshot.getUser());
    }

    private final Long id;
    private final String name;    // account name - no whitespaces allowed
    private final String description;
    private final Double actualBalance;   // actual account balance
//    private final Set<BillSource> bills;
    private final UserSource user;


    private Account(
            final Long id
            , final String name
            , final String description
            , final Double actualBalance
//            , final Set<BillSource> bills
            , final UserSource user)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
//        this.bills = bills;
        this.user = user;
    }

    AccountSnapshot getSnapshot()
    {
        return new AccountSnapshot(
                this.id
                , this.name
                , this.description
                , this.actualBalance
//                , this.bills
                , this.user);
    }
}
