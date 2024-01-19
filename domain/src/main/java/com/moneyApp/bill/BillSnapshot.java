package com.moneyApp.bill;

import com.moneyApp.vo.AccountSource;
import com.moneyApp.vo.BudgetSource;
import com.moneyApp.vo.PayeeSource;
import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BillSnapshot
{
    private Long id;
    private LocalDate billDate;
    private String number;  // format yearMonthValue_number(counted bills in given month + 1)
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
            , final LocalDate billDate
            , final String number
            , final PayeeSource payee
            , final AccountSource account
            , final BudgetSource budget
            , final String description
            , final Set<BillPositionSnapshot> positions
            , final UserSource user)
    {
        this.id = id;
        this.billDate = billDate;
        this.number = generateNumber(number);
        this.payee = payee;
        this.account = account;
        this.budget = budget;
        this.description = description;
        this.positions.addAll(positions);
        this.user = user;
    }

//    TODO rozwazania
    String generateNumber(String number)
    {
        if (!number.contains("_"))
            return String.format("%s%s_%s", this.billDate.getYear(), this.billDate.getMonthValue(), number);

        return number;
    }

    double getBillSum()
    {
        if (!this.positions.isEmpty())
            return this.positions
                .stream()
                .mapToDouble(BillPositionSnapshot::getAmount)
                .sum();

        return 0;
    }

    long getExemplaryCategoryId()
    {
        return this.positions
                .stream()
                .toList()
                .get(0).getCategoryId();
    }

    List<BillPositionSnapshot> filterPositionsWithoutBudgetPositions()
    {
        return this.positions
                .stream()
                .filter(p -> p.getBudgetPosition() == null)
                .toList();
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

    List<Long> getGainerIds()
    {
        return this.positions
                .stream()
                .map(BillPositionSnapshot::getGainerId)
                .toList();
    }

    UserSource getUser()
    {
        return this.user;
    }
}