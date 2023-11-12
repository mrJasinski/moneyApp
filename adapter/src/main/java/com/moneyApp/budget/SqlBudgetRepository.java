package com.moneyApp.budget;

import com.moneyApp.budget.Budget;
import com.moneyApp.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SqlBudgetRepository extends BudgetRepository, JpaRepository<Budget, Long>
{
    @Override
    @Query(value = "SELECT b.id FROM Budget b WHERE b.monthYear = :monthYear AND b.user.id = :userId")
    Optional<Long> findIdByMonthYearAndUserId(LocalDate monthYear, Long userId);

    @Override
    @Query(value = "SELECT b.user.id FROM Budget b WHERE b.id = :budgetId")
    Optional<Long> findUserIdByBudgetId(Long budgetId);

    @Override
    @Query(value = "SELECT b.monthYear FROM Budget b WHERE b.id = :budgetId")
    Optional<LocalDate> findMonthYearByBudgetId(long budgetId);

    @Override
    @Query(value = "SELECT * FROM budgets WHERE user_id = :userId ORDER BY month_year DESC LIMIT :number", nativeQuery = true)
    List<Budget> findLatestBudgetsByAmountAndUserId(Integer number, Long userId);

    @Override
    @Query(value = "SELECT COUNT(b) FROm Budget b WHERE b.user.id = :userId")
    Integer findAmountOfBudgetsByUserId(Long userId);

    @Override
    @Query(value = "FROM BudgetPosition p WHERE MONTH(p.budget.monthYear) = MONTH(:date) AND YEAR(p.budget.monthYear) = " +
            "YEAR(:date) AND p.category = :category AND p.budget.user.id = :userId")
    Optional<Budget.Position> findBudgetPositionByDateAndCategoryAndUserId(LocalDate date, Category category, Long userId);
}
