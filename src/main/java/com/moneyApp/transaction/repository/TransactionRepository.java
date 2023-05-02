package com.moneyApp.transaction.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.transaction.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository
{
    List<Transaction> findTransactionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, long userId);

    Transaction save(Transaction entity);

    Optional<Long> findHighestNumberByBillId(Long billId);

    void updatePositionIdInDb(Long transactionId, BudgetPosition position, Long userId);
}
