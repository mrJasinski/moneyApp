package com.moneyApp.bill.dto;

import com.moneyApp.vo.BudgetPositionSource;
import com.moneyApp.vo.CategorySource;

public interface BillPositionWithIdAndBudgetPositionIdDTO
{
    Long getId();

    BudgetPositionSource getBudgetPosition();

    CategorySource getCategory();

}
