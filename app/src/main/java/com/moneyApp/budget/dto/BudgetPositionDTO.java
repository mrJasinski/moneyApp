package com.moneyApp.budget.dto;

import com.moneyApp.category.CategoryType;
import com.moneyApp.category.dto.CategoryDTO;
import com.moneyApp.vo.BudgetPositionSource;

public class BudgetPositionDTO
{
    private Long id;
    private CategoryDTO category;
    private String categoryName;
    private CategoryType type;
    private double plannedAmount;
    private double actualAmount;
    private double amountSum;
    private String description;

    public BudgetPositionDTO(
            CategoryDTO category
            , Double plannedAmount
            , Double actualAmount
            , String description)
    {
        this.category = category;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.description = description;
    }

    public Long getId()
    {
        return this.id;
    }

    public CategoryDTO getCategory()
    {
        return this.category;
    }

    public CategoryType getType()
    {
        return this.type;
    }

    public double getPlannedAmount()
    {
        return this.plannedAmount;
    }

    public double getActualAmount()
    {
        return this.actualAmount;
    }

    public String getDescription()
    {
        return this.description;
    }
}
