package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillWithSumsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
interface SqlBillQueryRepository extends BillQueryRepository, JpaRepository<BillSnapshot, Long>
{
    @Override
    @Query(value = "SELECT COUNT (*) FROM BillSnapshot b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.user.id = :userId")
    Integer findBillCountBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "SELECT b.budgetPosition.id AS budgetPositionId, SUM(b.amount) AS sum FROM BillPositionSnapshot b WHERE b.id IN :billPosIds GROUP BY b.budgetPosition.id")
    Set<BillWithSumsDTO> findBudgetPositionsIdsWithSumsByBillPositionIds(List<Long> billPosIds);

    @Override
    @Query(value = "FROM BillSnapshot b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.user.id = :userId")
    List<BillSnapshot> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "SELECT bp.id FROM BillPositionSnapshot bp WHERE bp.budgetPosition.id = :budPosId")
    Set<Long> findBillPositionIdsByBudgetPositionId(Long budPosId);
}
