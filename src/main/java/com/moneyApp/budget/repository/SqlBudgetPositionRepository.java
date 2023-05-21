package com.moneyApp.budget.repository;

import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SqlBudgetPositionRepository extends BudgetPositionRepository, JpaRepository<BudgetPosition, Long>
{
    @Override
    @Query(value = "FROM BudgetPosition p WHERE MONTH(p.budget.monthYear) = MONTH(:date) AND YEAR(p.budget.monthYear) = " +
            "YEAR(:date) AND p.category = :category AND p.budget.user.id = :userId")
    Optional<BudgetPosition> findBudgetPositionByDateAndCategoryAndUserId(LocalDate date, Category category, Long userId);
}
