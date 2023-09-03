package com.moneyApp.bill.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.bill.BillPosition;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BillPositionRepository
{
    List<BillPosition> findTransactionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, long userId);
    List<BillPosition> findTransactionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);
    List<BillPosition> findTransactionsWithoutBudgetPositionByUserId(Long userId);

    BillPosition save(BillPosition entity);

    Optional<Long> findHighestNumberByBillId(Long billId);

    void updatePositionIdInDb(Long transactionId, BudgetPosition position, Long userId);


}
