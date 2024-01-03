package com.moneyApp.bill;

import com.moneyApp.vo.AccountSource;
import com.moneyApp.vo.BudgetSource;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

class BillSnapshot
{
    private Long id;
    private String number;  // format yearMonthValue_number(counted bills in given month + 1)
    private LocalDate billDate;
    private PayeeSource payee;
    private AccountSource account;
    private BudgetSource budget;
    private String description;
    private final Set<BillPositionSnapshot> positions = new HashSet<>();
    private UserSource user;

//    persistence constructor
    public BillSnapshot()
    {
    }

    BillSnapshot(
            final Long id
            , final String number
            , final LocalDate billDate
            , final PayeeSource payee
            , final AccountSource account
            , final BudgetSource budget
            , final String description
            , final Set<BillPositionSnapshot> positions
            , final UserSource user)
    {
        this.id = id;
        this.number = number;
        this.billDate = billDate;
        this.payee = payee;
        this.account = account;
        this.budget = budget;
        this.description = description;
        this.positions.addAll(positions);
        this.user = user;
    }

    Long getId()
    {
        return this.id;
    }

    String getNumber()
    {
        return this.number;
    }

    LocalDate getBillDate()
    {
        return this.billDate;
    }

    PayeeSource getPayee()
    {
        return this.payee;
    }

    Long getPayeeId()
    {
        return this.payee.getId();
    }

    AccountSource getAccount()
    {
        return this.account;
    }

    Long getAccountId()
    {
        return this.account.getId();
    }

    BudgetSource getBudget()
    {
        return this.budget;
    }

    String getDescription()
    {
        return this.description;
    }

    Set<BillPositionSnapshot> getPositions()
    {
        return this.positions;
    }

    UserSource getUser()
    {
        return this.user;
    }
}