package com.moneyApp.bill;

import com.moneyApp.vo.*;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

class Bill
{
    static Bill restore(BillSnapshot snapshot)
    {
        return new Bill(
                snapshot.getId()
                , snapshot.getNumber()
                , snapshot.getBillDate()
                , snapshot.getPayee()
                , snapshot.getAccount()
                , snapshot.getBudget()
                , snapshot.getDescription()
                , snapshot.getPositions().stream().map(Bill.Position::restore).collect(Collectors.toSet())
                , snapshot.getUser()
        );
    }

    private final Long id;
    private final String number;  // format yearMonthValue_number(counted bills in given month + 1)
    private final LocalDate billDate;
    private final PayeeSource payee;
    private final AccountSource account;
    private final BudgetSource budget;
    private final String description;
    private final Set<Position> billPositions;
    private final UserSource user;

    Bill(
            final Long id
            , final String number
            , final LocalDate billDate
            , final PayeeSource payee
            , final AccountSource account
            , final BudgetSource budget
            , final String description
            , final Set<Position> billPositions
            , final UserSource user)
    {
        this.id = id;
        this.number = number;
        this.billDate = billDate;
        this.payee = payee;
        this.account = account;
        this.budget = budget;
        this.description = description;
        this.billPositions = billPositions;
        this.user = user;
    }

    BillSnapshot getSnapshot()
    {
        return new BillSnapshot(
                this.id
                , this.billDate
                , this.number
                , this.payee
                , this.account
                , this.budget
                , this.description
                , this.billPositions.stream().map(Bill.Position::getSnapshot).collect(Collectors.toSet())
                , this.user
        );
    }

    static class Position
    {
        static Bill.Position restore(BillPositionSnapshot snapshot)
        {
            return new Bill.Position(
                    snapshot.getId()
                    , snapshot.getNumber()
                    , snapshot.getAmount()
                    , snapshot.getCategory()
                    , snapshot.getGainer()
                    , snapshot.getDescription()
                    , snapshot.getBudgetPosition()
            );
        }

        private final Long id;
        private final long number;  // subsequent number in given bill TODO zmiana na podobny do bill?
        private final Double amount;
        private final CategorySource category;
        private final PayeeSource gainer;
        private final String description;
        private final BudgetPositionSource budgetPosition;

        Position(
                final Long id
                , final long number
                , final Double amount
                , final CategorySource category
                , final PayeeSource gainer
                , final String description
                , final BudgetPositionSource budgetPosition)
        {
            this.id = id;
            this.number = number;
            this.amount = amount;
            this.category = category;
            this.gainer = gainer;
            this.description = description;
            this.budgetPosition = budgetPosition;
        }

        BillPositionSnapshot getSnapshot()
        {
            return new BillPositionSnapshot(
                    this.id
                    , this.number
                    , this.amount
                    , this.category
                    , this.gainer
                    , this.description
                    , this.budgetPosition
            );
        }
    }
}
