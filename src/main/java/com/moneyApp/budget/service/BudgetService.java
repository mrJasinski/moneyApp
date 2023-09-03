package com.moneyApp.budget.service;

import com.moneyApp.budget.Budget;
import com.moneyApp.budget.CalculationType;
import com.moneyApp.budget.MonthType;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPlanDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BudgetService
{
    void getBudgetDtoAmounts(BudgetDTO dto, Long userId);

    Long getBudgetIdByNumberAndUserId(String number, Long userId);

    LocalDate getBudgetMonthYearByNumber(String number);
    LocalDate getBudgetMonthYearById(long budgetId);

    long getUserIdByBudgetId(long budgetId);

    Integer getAmountOfBudgetsUserId(Long userId);

    List<Budget> getAllBudgets();
    List<Budget> getBudgetsByUserId(Long userId);
    List<Budget> getLatestBudgetsByAmount(Integer number, Long userId);

    List<BudgetDTO> getAllBudgetsAsDto();
    List<BudgetDTO> getBudgetForList(String email, Long userId);
    List<BudgetDTO> getBudgetsByUserEmailAsDto(String email);
    List<BudgetDTO> mapToDtoAndSetAmountsForBudgetsList(List<Budget> budgets);

    Budget getBudgetByNumberAndUserId(String number, long userId);
    Budget getBudgetByDateAndUserId(LocalDate date, Long userId);
    Budget createBudgetByUserEmail(BudgetDTO toSave, MonthType monthType, CalculationType calculationType, Integer monthsCount, String email);
    Budget getBudgetById(long budgetId);
    Budget getBudgetByMonthYearAndUserId(LocalDate date, Long userId);

    BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, long userId);
    BudgetDTO getBudgetByDateAndUserIdAsDto(LocalDate date, Long userId);
    BudgetDTO getBudgetByNumberAndUserEmailAsDto(String number, String email);

    BudgetPlanDTO getBudgetPlannedAsCopyOfMonth(LocalDate date, String email);
    BudgetPlanDTO getBudgetPlannedAsCopyByMethod(Integer number, CalculationType type, String email);
}
