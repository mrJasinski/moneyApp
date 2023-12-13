package com.moneyApp.budget;

import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.Set;

class BudgetSnapshot
{
    private Long id;
    private LocalDate monthYear;    //  day always set to 1 because is ignored
    private String description;
    private UserSource user;
    private Set<BudgetPositionSnapshot> positions;

    public BudgetSnapshot()
    {
    }

    BudgetSnapshot(
            final Long id
            , final LocalDate monthYear
            , final String description
            , final UserSource user
            , final Set<BudgetPositionSnapshot> positions)
    {
        this.id = id;
        this.monthYear = monthYear;
        this.description = description;
        this.user = user;
        this.positions = positions;
    }

    void addPositions(Set<BudgetPositionSnapshot> positions)
    {
        this.positions.addAll(positions);
    }

    public Long getId()
    {
        return this.id;
    }

    public LocalDate getMonthYear()
    {
        return this.monthYear;
    }

    public String getDescription()
    {
        return this.description;
    }

    public UserSource getUser()
    {
        return this.user;
    }

    public Set<BudgetPositionSnapshot> getPositions()
    {
        return this.positions;
    }
}
