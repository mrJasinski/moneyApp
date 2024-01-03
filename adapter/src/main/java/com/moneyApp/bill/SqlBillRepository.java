package com.moneyApp.bill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
interface SqlBillRepository extends BillRepository, JpaRepository<BillSnapshot, Long>
{
    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE bill_positions b " +
                   "SET b.budget_position_id = :budgetPositionId " +
                   "WHERE id IN :billPositionIds"
                    , nativeQuery = true)
    void updateBudgetPositionInBillPositionByIds(Long budgetPositionId, List<Long> billPositionIds);

    @Transactional
    @Modifying
    @Override
    @Query(value = "UPDATE BillSnapshot b " +
                   "SET b.budget.id = :budgetId " +
                   "WHERE b.id IN :billIds")
    void updateBudgetInBills(long budgetId, List<Long> billIds);
}
