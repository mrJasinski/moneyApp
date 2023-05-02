package com.moneyApp.budget.repository;


import com.moneyApp.budget.Budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BudgetRepository
{
    Budget save(Budget budget);

    List<Budget> findAll();
    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByMonthYear(LocalDate date);
    Optional<Budget> findByMonthYearAndUserId(LocalDate date, Long userId);
    Optional<Budget> findById(Long budgetId);

    Boolean existsByMonthYear(LocalDate monthYear);
    Boolean existsByMonthYearAndUserId(LocalDate monthYear, Long userId);

    Optional<Long> findIdByMonthYearAndUserId(LocalDate monthYear, Long userId);
    Optional<Long> findUserIdByBudgetId(Long budgetId);

    Optional<LocalDate> findMonthYearByBudgetId(long budgetId);


}
