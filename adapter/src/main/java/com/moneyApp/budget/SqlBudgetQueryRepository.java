package com.moneyApp.budget;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SqlBudgetQueryRepository extends BudgetQueryRepository, JpaRepository<BudgetSnapshot, Long>
{

}
