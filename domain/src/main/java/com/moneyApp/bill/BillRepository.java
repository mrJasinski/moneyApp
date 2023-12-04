package com.moneyApp.bill;

public interface BillRepository
{
    Bill save(Bill entity);

    void deleteByNumberAndUserId(String number, Long userId);
//    void updatePositionIdInDb(Long transactionId, Long budgetPositionId, Long userId);
}
