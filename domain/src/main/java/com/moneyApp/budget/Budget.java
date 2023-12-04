package com.moneyApp.budget;

import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;
import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

class Budget
{
    static Budget restore(BudgetSnapshot snapshot)
    {
        return new Budget(
                snapshot.getId()
                , snapshot.getMonthYear()
                , snapshot.getDescription()
                , snapshot.getUser()
                , snapshot.getPositions()
                    .stream()
                    .map(Position::restore)
                    .collect(Collectors.toSet())
        );
    }

    private final Long id;
    private final LocalDate monthYear;    //  day always set to 1 because is ignored
    private final String description;
    private final UserSource user;
    private final Set<Budget.Position> positions;

    Budget(
            final Long id
            , final LocalDate monthYear
            , final String description
            , final UserSource user
            , final Set<Position> positions)
    {
        this.id = id;
        this.monthYear = monthYear;
        this.description = description;
        this.user = user;
        this.positions = positions;
    }

    BudgetSnapshot getSnapshot()
    {
        return new BudgetSnapshot(
                this.id
                , this.monthYear
                , this.description
                , this.user
                , this.positions
                    .stream()
                    .map(Position::getSnapshot)
                    .collect(Collectors.toSet())

        );
    }

    static class Position
    {
        static Position restore(BudgetPositionSnapshot snapshot)
        {
            return new Position(
                    snapshot.getId()
                    , snapshot.getCategory()
                    , snapshot.getPlannedAmount()
                    , snapshot.getDescription()
                    , snapshot.getBillPositions()
            );
        }

        private final Long id;
        private final CategorySource category;
        private final Double plannedAmount;
        private final String description;
        private final Set<BillPositionSource> billPositions;

        Position(
                final Long id
                , final CategorySource category
                , final Double plannedAmount
                , final String description
                , final Set<BillPositionSource> billPositions)
        {
            this.id = id;
            this.category = category;
            this.plannedAmount = plannedAmount;
            this.description = description;
            this.billPositions = billPositions;
        }

        BudgetPositionSnapshot getSnapshot()
        {
            return new BudgetPositionSnapshot(
                    this.id
                    , this.category
                    , this.plannedAmount
                    , this.description
                    , this.billPositions
            );
        }
    }
}
