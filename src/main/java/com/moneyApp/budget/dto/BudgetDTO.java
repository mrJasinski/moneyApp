package com.moneyApp.budget.dto;

import com.moneyApp.budget.Budget;

import java.time.LocalDate;
import java.util.List;

public class BudgetDTO
{
    private String number;  // miesiąc i rok np 122023
    private LocalDate monthYear;
    private double plannedIncomes;
    private double plannedExpenses;
    private double plannedSum;
    private double actualIncomes;
    private double actualExpenses;
    private double actualSum;
    private double incomesSum;
    private double expensesSum;
    private List<BudgetPositionDTO> incomes;
    private List<BudgetPositionDTO> expenses;
    private String description;

    public BudgetDTO()
    {
    }

    public BudgetDTO(LocalDate monthYear, String description)
    {
        this.monthYear = monthYear;
        if (this.monthYear.getMonth().getValue() < 10)
            this.number = String.format("0%s%s", this.monthYear.getMonth().getValue(), this.monthYear.getYear());
        else
            this.number = String.format("%s%s", this.monthYear.getMonth().getValue(), this.monthYear.getYear());
        this.description = description;
    }

    public Budget toBudget()
    {
        return new Budget(this.monthYear, this.description);
    }

    public String getNumber()
    {
        return this.number;
    }

    public LocalDate getMonthYear()
    {
        return this.monthYear;
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

    public double getActualIncomes()
    {
        return this.actualIncomes;
    }

    public double getActualExpenses()
    {
        return this.actualExpenses;
    }

    public double getActualSum()
    {
        return this.actualSum;
    }

    public double getIncomesSum()
    {
        return this.incomesSum;
    }

    public double getExpensesSum()
    {
        return this.expensesSum;
    }

    public List<BudgetPositionDTO> getIncomes()
    {
        return this.incomes;
    }

    public List<BudgetPositionDTO> getExpenses()
    {
        return this.expenses;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void setPlannedIncomes(double plannedIncomes)
    {
        this.plannedIncomes = plannedIncomes;
    }

    public void setPlannedExpenses(double plannedExpenses)
    {
        this.plannedExpenses = plannedExpenses;
    }

    public void setPlannedSum(double plannedSum)
    {
        this.plannedSum = plannedSum;
    }

    public void setActualIncomes(double actualIncomes)
    {
        this.actualIncomes = actualIncomes;
    }

    public void setActualExpenses(double actualExpenses)
    {
        this.actualExpenses = actualExpenses;
    }

    public void setActualSum(double actualSum)
    {
        this.actualSum = actualSum;
    }

    public void setIncomesSum(double incomesSum)
    {
        this.incomesSum = incomesSum;
    }

    public void setExpensesSum(double expensesSum)
    {
        this.expensesSum = expensesSum;
    }

    public void setIncomes(List<BudgetPositionDTO> incomes)
    {
        this.incomes = incomes;
    }

    public void setExpenses(List<BudgetPositionDTO> expenses)
    {
        this.expenses = expenses;
    }

    public void setMonthYear(LocalDate monthYear)
    {
        this.monthYear = monthYear;
    }
}
