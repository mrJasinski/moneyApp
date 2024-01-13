package com.moneyApp.bill;

import java.util.List;
import java.util.Set;

interface BillRepository
{
    BillSnapshot save(BillSnapshot entity);

    void deleteByNumberAndUserId(String number, Long userId);

    void updateBudgetPositionInBillPositionByIds(Long budgetPositionId, Set<Long> billPositionIds);

    void updateBudgetInBills(long budgetId, List<Long> billIds);
}
