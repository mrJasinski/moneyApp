package com.moneyApp.budget;

import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface BudgetQueryRepository
{
    List<Budget> findAll();
    List<Budget> findByUserId(Long userId);
    List<Budget> findLatestBudgetsByAmountAndUserId(Integer number, Long userId);

    Optional<Budget> findByMonthYear(LocalDate date);
    Optional<Budget> findByMonthYearAndUserId(LocalDate date, Long userId);
    Optional<Budget> findById(Long budgetId);

    Boolean existsByMonthYear(LocalDate monthYear);
    Boolean existsByMonthYearAndUserId(LocalDate monthYear, Long userId);

    Optional<Long> findIdByMonthYearAndUserId(LocalDate monthYear, Long userId);
    Optional<Long> findUserIdByBudgetId(Long budgetId);

    Optional<LocalDate> findMonthYearByBudgetId(long budgetId);

    Integer findAmountOfBudgetsByUserId(Long userId);

    List<Budget.Position> findByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType);

    Optional<Budget.Position> findBudgetPositionByDateAndCategoryAndUserId(LocalDate date, Category category, Long userId);
}
