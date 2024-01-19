package com.moneyApp.budget;

import com.moneyApp.vo.UserSource;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class BudgetSnapshot
{
    private Long id;
    private LocalDate monthYear;    //  day always set to 1 because is ignored
    private String description;
    private UserSource user;
    private Set<BudgetPositionSnapshot> positions = new HashSet<>();

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
        addPositions(positions);
    }

    void addPositions(final Set<BudgetPositionSnapshot> positions)
    {
        this.positions.addAll(positions);
    }

    void addPosition(final BudgetPositionSnapshot position)
    {
        this.positions.add(position);
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

    Long getUserId()
    {
        return this.user.getId();
    }

    public Set<BudgetPositionSnapshot> getPositions()
    {
        return this.positions;
    }

    List<Long> getCategoriesIdsFromPositions()
    {
        return this.positions
                .stream()
                .map(BudgetPositionSnapshot::getCategoryId)
                .toList();
    }

    BudgetPositionSnapshot getPositionByCategoryId(long categoryId)
    {
        return this.positions
                .stream()
                .filter(p -> p.getCategoryId().equals(categoryId))
                .toList()
                .get(0);
    }
}
