package com.moneyApp.bill.dto;

import com.moneyApp.vo.BudgetSource;

public interface BillWithIdAndBudgetIdDTO
{
    Long getId();

    BudgetSource getBudgetSource();
}
