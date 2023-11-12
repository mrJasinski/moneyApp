package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

interface BillQueryRepository
{
    Optional<Long> findHighestBillNumberByMonthYearAndUserId(int month, int year, long userId);

    List<BillDTO> findByUserId(Long userId);

    List<BillPositionDTO> findBillPositionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
    List<BillPositionDTO> findBillPositionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);
    List<BillPositionDTO> findBillPositionsWithoutBudgetPositionByUserId(Long userId);

    Optional<Long> findHighestNumberByBillId(Long billId);
}