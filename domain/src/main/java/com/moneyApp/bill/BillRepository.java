package com.moneyApp.bill;

interface BillRepository
{
    BillSnapshot save(BillSnapshot entity);

    void deleteByNumberAndUserId(String number, Long userId);

    void updateBudgetIdInBillByNumber(String number, long budgetId);
//    void updatePositionIdInDb(Long transactionId, Long budgetPositionId, Long userId);
}
