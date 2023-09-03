package com.moneyApp.budget.service;

import com.moneyApp.bill.BillPosition;
import com.moneyApp.budget.Budget;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import com.moneyApp.category.Category;
import com.moneyApp.category.CategoryType;

import java.util.List;

public interface BudgetPositionService
{
    List<BudgetPosition> getBudgetPositionsByBudgetIdAndCategoryType(long budgetId, CategoryType categoryType);

    List<BudgetPositionDTO> getBudgetPositionsByBudgetIdAndCategoryTypeAsDto(long budgetId, CategoryType categoryType);
    List<BudgetPositionDTO> convertBudgetPositionsToDto(List<BudgetPosition> positions);

    void assignBillPositionsToBudgetPositions(List<BillPosition> billPositions, List<BudgetPosition> positions, long userId);

    void createBudgetPositionsByCategories(List<Category> categories, List<Category> posCategories,
                                           List<BudgetPosition> positions, long budgetId);

    BudgetPosition createBudgetPosition(Category category, Budget budget);

    double sumBillPositionsInBudgetPositionsList(List<BudgetPosition> positions);
}
