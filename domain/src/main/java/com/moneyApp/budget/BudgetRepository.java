package com.moneyApp.budget;

import java.time.LocalDate;
import java.util.List;

interface BudgetRepository
{
    BudgetSnapshot save(BudgetSnapshot budget);

    BudgetPositionSnapshot save(BudgetPositionSnapshot position);

    void save(List<BudgetPositionSnapshot> toSave);

    void deleteByMonthYearAndUserId(LocalDate number, Long userId);
}
