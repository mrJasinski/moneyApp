package com.moneyApp.budget;

import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;

import java.util.HashSet;
import java.util.Set;

class BudgetPositionSnapshot
{
    private Long id;
    private CategorySource category;
    private Double plannedAmount;
    private String description;
    private BudgetSnapshot budget;
    private final Set<BillPositionSource> billPositions = new HashSet<>();

    BudgetPositionSnapshot()
    {
    }

    BudgetPositionSnapshot(
            final Long id
            , final CategorySource category
            , final Double plannedAmount
            , final String description
            , final BudgetSnapshot budget
            , final Set<BillPositionSource> billPositions)
    {
        this.id = id;
        this.category = category;
        this.plannedAmount = plannedAmount;
        this.description = description;
        this.budget = budget;
        addBillPositionSources(billPositions);
    }

    void addBillPositionSources(Set<BillPositionSource> sources)
    {
        if (sources == null)
            sources = new HashSet<>();

        this.billPositions.addAll(sources);
    }

    void addBillPositionSource(final BillPositionSource billPositionSource)
    {
        this.billPositions.add(billPositionSource);
    }

    public Long getId()
    {
        return this.id;
    }

    public CategorySource getCategory()
    {
        return this.category;
    }

    Long getCategoryId()
    {
        return this.category.getId();
    }

    public Double getPlannedAmount()
    {
        return this.plannedAmount;
    }

    public String getDescription()
    {
        return this.description;
    }

    public BudgetSnapshot getBudget()
    {
        return this.budget;
    }

    public Set<BillPositionSource> getBillPositions()
    {
        return this.billPositions;
    }
}
