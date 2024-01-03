package com.moneyApp.bill;

import java.util.List;

interface BillRepository
{
    BillSnapshot save(BillSnapshot entity);

    void deleteByNumberAndUserId(String number, Long userId);

    void updateBudgetPositionInBillPositionByIds(Long budgetPositionId, List<Long> billPositionIds);

    void updateBudgetInBills(long budgetId, List<Long> billIds);
}
