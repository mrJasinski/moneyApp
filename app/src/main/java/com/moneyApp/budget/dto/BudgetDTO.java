package com.moneyApp.budget.dto;

import com.moneyApp.category.CategoryType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BudgetDTO
{
    private String number;  // month and year e.g. 122023
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
    private List<BudgetPositionDTO> positions;

    public BudgetDTO()
    {
    }

    public BudgetDTO(LocalDate monthYear, String description)
    {
        this.monthYear = monthYear;
        this.number = generateBudgetNumber(monthYear);
        this.description = description;
    }

    public BudgetDTO(
            LocalDate monthYear
            , double plannedIncomes
            , double actualIncomes
            , double plannedExpenses
            , double actualExpenses)
    {
        this.monthYear = monthYear;
        this.plannedIncomes = plannedIncomes;
        this.actualIncomes = actualIncomes;
        this.plannedExpenses = plannedExpenses;
        this.actualExpenses = actualExpenses;
        this.actualSum = this.plannedIncomes - this.plannedExpenses;
    }

//    private Long id;
//    private LocalDate monthYear;    //  day always set to 1 because is ignored
//    private String description;
//    private UserSource user;
//    private Set<BudgetPositionSnapshot> positions;

    public BudgetDTO(LocalDate monthYear, String description, List<BudgetPositionDTO> positions)
    {
        this(monthYear, description);
        this.expenses = positions
                .stream()
                .filter(p -> p.getCategory().getType().equals(CategoryType.INCOME))
                .collect(Collectors.toList());
        this.incomes = positions
                .stream()
                .filter(p -> p.getCategory().getType().equals(CategoryType.EXPENSE))
                .collect(Collectors.toList());
    }



    private String generateBudgetNumber(final LocalDate monthYear)
    {
        var number = String.format("%s%s", monthYear.getMonth().getValue(), monthYear.getYear());
        if (monthYear.getMonth().getValue() < 10)
            number = "0" + number;

        return number;
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

    public List<BudgetPositionDTO> getPositions()
    {
        return this.positions;
    }
}
