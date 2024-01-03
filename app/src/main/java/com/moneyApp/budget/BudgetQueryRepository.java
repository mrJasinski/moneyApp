package com.moneyApp.budget;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface BudgetQueryRepository
{

    Optional<BudgetSnapshot> findByMonthYearAndUserId(LocalDate date, Long userId);

    boolean existsByMonthYearAndUserId(LocalDate monthYear, Long userId);

    List<BudgetSnapshot> findAllByUserId(Long userId);

}
