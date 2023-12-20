package com.moneyApp.budget;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
interface SqlBudgetRepository extends BudgetRepository, JpaRepository<BudgetSnapshot, Long>
{
    @Modifying
    @Transactional
    @Override
    @Query(value = "DELETE " +
                   "FROM BudgetSnapshot b " +
                   "WHERE b.monthYear = :monthYear AND b.user.id = :userId")
    void deleteByMonthYearAndUserId(LocalDate monthYear, Long userId);
}
