package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillWithSumsDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

interface BillQueryRepository
{
    List<BillSnapshot> findByUserId(Long userId);
    List<BillSnapshot> findByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    Optional<BillSnapshot> findByNumberAndUserId(String number, Long userId);

    Integer findBillCountBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    boolean existsByNumberAndUserId(String number, Long userId);

    Set<BillWithSumsDTO> findBudgetPositionsIdsWithSumsByBillPositionIds(List<Long> billPosIds);

    Set<Long> findBillPositionIdsByBudgetPositionId(Long budPosId);
}