package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionView;
import com.moneyApp.bill.dto.BillView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

interface BillQueryRepository
{
//    Optional<Long> findHighestBillNumberByMonthYearAndUserId(int month, int year, long userId);

    List<BillSnapshot> findByUserId(Long userId);
//    List<BillView> findByUserIdAsView(Long userId);

//    List<BillPositionDTO> findBillPositionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
//    List<BillPositionDTO> findBillPositionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);
//    List<BillPositionDTO> findBillPositionsWithoutBudgetPositionByUserId(Long userId);
//
//    Optional<Long> findHighestNumberByBillId(Long billId);
    Optional<BillSnapshot> findByNumberAndUserId(String number, Long userId);
//    Optional<BillView> findByNumberAndUserIdAsView(String number, Long userId);

    Integer findBillCountBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    boolean existsByNumberAndUserId(String number, Long userId);

    Double findBillPositionsSumByBudgetPositionId(Long budgetPositionId);

    List<BillSnapshot> findWithNullBudgetId();

    List<BillSnapshot> findByBudgetId(Long budgetId);

    Map<Long, Double> findBillPositionsSumsWithBudgetPositionIdByBudgetPositionId(List<Long> budgetPositionsIds);

//    List<BillPositionSnapshot> findPositionsByBillId(Long billId);
//    List<BillPositionView> findPositionsByBillIdAsView(Long billId);
}