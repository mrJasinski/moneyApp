package com.moneyApp.transaction.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SqlTransactionRepository extends TransactionRepository, JpaRepository<Transaction, Long>
{
    @Override
    @Query(value = "FROM Transaction t WHERE (t.bill.billDate BETWEEN :startDate AND :endDate) AND t.user.id = :userId")
    List<Transaction> findTransactionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, long userId);

    @Override
    @Query(value = "SELECT MAX(t.number) FROM Transaction t WHERE t.bill.id = :billId")
    Optional<Long> findHighestNumberByBillId(Long billId);

    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE Transaction t SET t.position = :position WHERE t.id = :transactionId AND t.user.id = :userId")
    void updatePositionIdInDb(Long transactionId, BudgetPosition position, Long userId);

    @Override
    @Query(value = "FROM Transaction t WHERE t.bill.billDate BETWEEN :startDate AND :endDate AND t.user.id = :userId AND t.position = null")
    List<Transaction> findTransactionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "FROM Transaction t WHERE t.user.id = :userId AND t.position = null")
    List<Transaction> findTransactionsWithoutBudgetPositionByUserId(Long userId);
}
