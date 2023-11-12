package com.moneyApp.budget;

public interface BudgetRepository
{
    Budget save(Budget budget);

    Budget.Position save(Budget.Position entity);
}
