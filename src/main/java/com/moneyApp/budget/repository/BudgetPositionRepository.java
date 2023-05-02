package com.moneyApp.budget.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.CategoryType;

import java.util.List;

public interface BudgetPositionRepository
{
    List<BudgetPosition> findByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType);

    BudgetPosition save(BudgetPosition entity);
}
