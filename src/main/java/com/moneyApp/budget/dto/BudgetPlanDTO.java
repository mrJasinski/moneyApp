package com.moneyApp.budget.dto;

import com.moneyApp.budget.Budget;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.CategoryType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BudgetPlanDTO
{
    private double plannedIncomes;
    private double plannedExpenses;
    private double plannedSum;
    private final List<BudgetPositionDTO> incomes;
    private final List<BudgetPositionDTO> expenses;

    public BudgetPlanDTO(Budget budget)
    {
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();

        for (BudgetPosition bp : budget.getPositions())
        {
            if ((bp.getCategory().getType()).equals(CategoryType.INCOME))
                this.incomes.add(new BudgetPositionDTO(bp.getCategory().getCategoryName(), bp.getPlannedAmount()));

            if ((bp.getCategory().getType()).equals(CategoryType.EXPENSE))
                this.expenses.add(new BudgetPositionDTO(bp.getCategory().getCategoryName(), bp.getPlannedAmount()));
        }

        this.plannedIncomes = this.incomes.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum();
        this.plannedExpenses = this.expenses.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum();
        this.plannedSum = this.plannedIncomes - this.plannedExpenses;
    }

    public BudgetPlanDTO(List<BudgetPositionDTO> positions)
    {
        this.incomes = new ArrayList<>();
        this.expenses = new ArrayList<>();

        for (BudgetPositionDTO bp : positions)
        {
            if ((bp.getCategory().getType()).equals(CategoryType.INCOME))
                this.incomes.add(bp);

            if ((bp.getCategory().getType()).equals(CategoryType.EXPENSE))
                this.expenses.add(bp);
        }

        this.plannedIncomes = this.incomes.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum();
        this.plannedExpenses = this.expenses.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum();
        this.plannedSum = this.plannedIncomes - this.plannedExpenses;
    }

    public double getPlannedIncomes()
    {
        return this.plannedIncomes;
    }

    public double getPlannedExpenses()
    {
        return this.plannedExpenses;
    }

    public double getPlannedSum()
    {
        return this.plannedSum;
    }

    public List<BudgetPositionDTO> getIncomes()
    {
        return this.incomes;
    }

    public List<BudgetPositionDTO> getExpenses()
    {
        return this.expenses;
    }
}
