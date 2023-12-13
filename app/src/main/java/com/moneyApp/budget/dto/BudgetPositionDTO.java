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


    public BudgetPositionDTO(String category, Double plannedAmount)
    {
        this.categoryName = category;
        this.plannedAmount = plannedAmount;
    }

    public BudgetPositionDTO(CategoryDTO category, Double plannedAmount, String description)
    {
        this.category = category;
        this.plannedAmount = plannedAmount;
        this.description = description;
    }

    public BudgetPositionDTO(String categoryName, Double plannedAmount, Double actualAmount, String description)
    {
        this.categoryName = categoryName;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.description = description;
    }

    public BudgetPositionDTO(
            String categoryName
            , CategoryType type
            , Double plannedAmount
            , Double actualAmount
            , String description)
    {
        this.categoryName = categoryName;
        this.type = type;
        this.plannedAmount = plannedAmount;
        this.actualAmount = actualAmount;
        this.description = description;
    }

    public BudgetPositionSource toSource()
    {
        return new BudgetPositionSource(this.id);
    }

    public Long getId()
    {
        return this.id;
    }

    public CategoryDTO getCategory()
    {
        return this.category;
    }

     public String getCategoryName()
    {
        return this.categoryName;
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
