package com.moneyApp.budget.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BudgetPositionRepository
{
    List<BudgetPosition> findByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType);

    BudgetPosition save(BudgetPosition entity);

    Optional<BudgetPosition> findBudgetPositionByDateAndCategoryAndUserId(LocalDate date, Category category, Long userId);
}
