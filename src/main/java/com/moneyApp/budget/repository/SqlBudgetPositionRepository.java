package com.moneyApp.budget.repository;

import com.moneyApp.budget.BudgetPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SqlBudgetPositionRepository extends BudgetPositionRepository, JpaRepository<BudgetPosition, Long>
{
}
