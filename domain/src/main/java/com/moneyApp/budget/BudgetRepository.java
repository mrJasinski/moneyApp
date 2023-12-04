package com.moneyApp.budget;

import java.time.LocalDate;

interface BudgetRepository
{
    BudgetSnapshot save(BudgetSnapshot budget);

    void deleteByMonthYearAndUserId(LocalDate number, Long userId);
}
