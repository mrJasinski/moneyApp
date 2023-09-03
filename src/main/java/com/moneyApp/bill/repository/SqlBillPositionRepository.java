package com.moneyApp.bill.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.bill.BillPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SqlBillPositionRepository extends BillPositionRepository, JpaRepository<BillPosition, Long>
{
    @Override
    @Query(value = "FROM BillPosition t WHERE (t.bill.billDate BETWEEN :startDate AND :endDate) AND t.user.id = :userId")
    List<BillPosition> findTransactionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, long userId);

    @Override
    @Query(value = "SELECT MAX(t.number) FROM BillPosition t WHERE t.bill.id = :billId")
    Optional<Long> findHighestNumberByBillId(Long billId);

    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE BillPosition t SET t.position = :position WHERE t.id = :transactionId AND t.user.id = :userId")
    void updatePositionIdInDb(Long transactionId, BudgetPosition position, Long userId);

    @Override
    @Query(value = "FROM BillPosition t WHERE t.bill.billDate BETWEEN :startDate AND :endDate AND t.user.id = :userId AND t.position = null")
    List<BillPosition> findTransactionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "FROM BillPosition t WHERE t.user.id = :userId AND t.position = null")
    List<BillPosition> findTransactionsWithoutBudgetPositionByUserId(Long userId);
}
