package com.moneyApp.budget;

import java.time.LocalDate;

interface BudgetRepository
{
    BudgetSnapshot save(BudgetSnapshot budget);

    BudgetPositionSnapshot save(BudgetPositionSnapshot position);

    void deleteByMonthYearAndUserId(LocalDate number, Long userId);
}
