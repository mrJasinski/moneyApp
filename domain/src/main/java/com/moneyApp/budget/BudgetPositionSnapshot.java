package com.moneyApp.budget;

import com.moneyApp.vo.BillPositionSource;
import com.moneyApp.vo.CategorySource;

import java.util.Set;

class BudgetPositionSnapshot
{
    private Long id;
    private CategorySource category;
    private Double plannedAmount;
    private String description;
    private Set<BillPositionSource> billPositions;

    BudgetPositionSnapshot()
    {
    }

    BudgetPositionSnapshot(
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

    public Long getId()
    {
        return this.id;
    }

    public CategorySource getCategory()
    {
        return this.category;
    }

    public Double getPlannedAmount()
    {
        return this.plannedAmount;
    }

    public String getDescription()
    {
        return this.description;
    }

    public Set<BillPositionSource> getBillPositions()
    {
        return this.billPositions;
    }
}
