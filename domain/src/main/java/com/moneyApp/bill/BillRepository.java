package com.moneyApp.bill;

public interface BillRepository
{
    Bill save(Bill entity);

    Bill.Position save(Bill.Position entity);

    void updatePositionIdInDb(Long transactionId, Long budgetPositionId, Long userId);
}
