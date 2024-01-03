package com.moneyApp.account;

import com.moneyApp.vo.BillSource;
import com.moneyApp.vo.UserSource;

import java.util.HashSet;
import java.util.Set;

class AccountSnapshot
{
    private Long id;
    private String name;    // account name - no whitespaces allowed
    private String description;
    private Double actualBalance;   // actual account balance
//    private Set<BillSource> bills = new HashSet<>();
    private UserSource user;


    //    persistence constructor
    public AccountSnapshot()
    {
    }

    AccountSnapshot(
            final Long id
            , final String name
            , final String description
            , final Double actualBalance
//            , final Set<BillSource> bills
            , final UserSource user)
    {
        this.id = id;
        checkNameForWhitespaces(name);
        this.name = name;
        this.description = description;
        this.actualBalance = actualBalance;
//        this.bills = bills;
        this.user = user;
    }

    void checkNameForWhitespaces(String name)
    {
        //        check if account name has whitespaces
        if  (name.contains(" "))
            throw new IllegalArgumentException("Account name cannot contain whitespaces!");
    }

    public Long getId()
    {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Double getActualBalance()
    {
        return this.actualBalance;
    }

//    public Set<BillSource> getBills()
//    {
//        return this.bills;
//    }

    public UserSource getUser()
    {
        return this.user;
    }
}
