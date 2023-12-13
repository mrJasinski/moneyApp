package com.moneyApp.budget.dto;

import com.moneyApp.category.CategoryType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private final List<BudgetPositionDTO> incomes = new ArrayList<>();
    private final List<BudgetPositionDTO> expenses = new ArrayList<>();
    private String description;
    private final List<BudgetPositionDTO> positions = new ArrayList<>();

    public BudgetDTO()
    {
    }

    public BudgetDTO(LocalDate monthYear, String description)
    {
        this.monthYear = checkIfMonthYearHasCorrectFormat(monthYear);
        this.number = generateBudgetNumber();
        this.description = description;
    }

    public BudgetDTO(
            LocalDate monthYear
            , double plannedIncomes
            , double actualIncomes
            , double plannedExpenses
            , double actualExpenses)
    {

        this.monthYear = checkIfMonthYearHasCorrectFormat(monthYear);
        this.plannedIncomes = plannedIncomes;
        this.actualIncomes = actualIncomes;
        this.plannedExpenses = plannedExpenses;
        this.actualExpenses = actualExpenses;
        this.actualSum = this.plannedIncomes - this.plannedExpenses;
    }

    public BudgetDTO(LocalDate monthYear, String description, List<BudgetPositionDTO> positions)
    {
        this(monthYear, description);
        addPositions(positions);
    }

    public void addPositions(List<BudgetPositionDTO> positions)
    {
//          add only positions that are not existing already
        positions.forEach(p -> {
            if (!this.positions.contains(p))
                this.positions.add(p);
        });

        splitPositionsIntoIncomesAndExpenses(positions);

        sumPositionsAmounts();
    }

    void splitPositionsIntoIncomesAndExpenses(List<BudgetPositionDTO> positions)
    {
        this.expenses.addAll(positions
                .stream()
                .filter(p -> p.getType().equals(CategoryType.EXPENSE))
                .toList());

        this.incomes.addAll(positions
                .stream()
                .filter(p -> p.getType().equals(CategoryType.INCOME))
                .toList());
    }

    void sumPositionsAmounts()
    {
        this.plannedIncomes = roundToTwoDecimals(this.incomes.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());
        this.plannedExpenses = roundToTwoDecimals(this.expenses.stream().mapToDouble(BudgetPositionDTO::getPlannedAmount).sum());

        this.actualIncomes = roundToTwoDecimals(this.incomes.stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());
        this.actualExpenses = roundToTwoDecimals(this.expenses.stream().mapToDouble(BudgetPositionDTO::getActualAmount).sum());

        this.plannedSum = roundToTwoDecimals(this.plannedIncomes - this.plannedExpenses);
        this.actualSum = roundToTwoDecimals(this.actualIncomes - this.actualExpenses);

        this.incomesSum = roundToTwoDecimals(this.plannedIncomes - this.actualIncomes);
        this.expensesSum = roundToTwoDecimals(this.plannedExpenses - this.actualExpenses);
    }

    private String generateBudgetNumber()
    {
        var number = String.format("%s%s", this.monthYear.getMonth().getValue(), this.monthYear.getYear());

        if (this.monthYear.getMonth().getValue() < 10)
            number = "0" + number;

        return number;
    }

    double roundToTwoDecimals(double toRound)
    {
        return Math.round(toRound * 100d) / 100d;
    }

    LocalDate checkIfMonthYearHasCorrectFormat(LocalDate monthYear)
    {
        if (monthYear.getDayOfMonth() != 1)
            monthYear = LocalDate.of(monthYear.getYear(), monthYear.getMonth().getValue(), 1);

        return monthYear;
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

    public List<BudgetPositionDTO> getPositions()
    {
        return this.positions;
    }
}
