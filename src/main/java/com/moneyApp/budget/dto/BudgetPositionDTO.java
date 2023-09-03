package com.moneyApp.budget.dto;

import com.moneyApp.category.Category;
import com.moneyApp.category.dto.CategoryDTO;

public class BudgetPositionDTO
{
    private CategoryDTO category;
    private double plannedAmount;
    private double actualAmount;
    private double amountSum;
    private String description;


    public BudgetPositionDTO(String category, Double plannedAmount)
    {
        this.category = new CategoryDTO(category);
        this.plannedAmount = plannedAmount;
    }

    public BudgetPositionDTO(Category category, Double plannedAmount, String description)
    {
        this.category = category.toDto();
        this.plannedAmount = plannedAmount;
        this.description = description;
    }

    public CategoryDTO getCategory()
    {
        return this.category;
    }

    public double getPlannedAmount()
    {
        return this.plannedAmount;
    }

    public double getActualAmount()
    {
        return this.actualAmount;
    }

    public double getAmountSum()
    {
        return this.amountSum;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setActualAmount(double actualAmount)
    {
        this.actualAmount = actualAmount;
    }

    public void setAmountSum(double amountSum)
    {
        this.amountSum = amountSum;
    }
}
