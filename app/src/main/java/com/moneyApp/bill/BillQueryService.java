package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
class BillQueryService
{
    private final BillQueryRepository billQueryRepo;

    BillQueryService(final BillQueryRepository billQueryRepo)
    {
        this.billQueryRepo = billQueryRepo;
    }

    long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId)
    {
        return this.billQueryRepo
                .findHighestBillNumberByMonthYearAndUserId(date.getMonth().getValue(), date.getYear(), userId)
                .orElse(0L);
    }

    List<BillDTO> getBillsByUserEmailAsDto(Long userId)
    {
        return this.billQueryRepo
                .findByUserId(userId);
    }

    long getHighestBillPositionNumberByBillId(long billId)
    {
//        read from db the highest position number in given bill
//        if no number found then assign 0
        return this.billQueryRepo
                .findHighestNumberByBillId(billId)
                .orElse(0L);
    }

    List<BillPositionDTO> getBillPositionsWithoutBudgetPositionByDateAndUserIdAsDto(LocalDate startDate, LocalDate endDate, Long userId)
    {
        return this.billQueryRepo
                .findBillPositionsBetweenDatesWithoutBudgetPositionByUserId(startDate, endDate, userId);
    }

    List<BillPositionDTO> getBillPositionsWithoutBudgetPositionsByUserIdAsDto(Long userId)
    {
        return this.billQueryRepo
                .findBillPositionsWithoutBudgetPositionByUserId(userId);
    }

    List<BillPositionDTO> getBillPositionsByDatesAndUserIdAsDto(LocalDate startDate, LocalDate endDate, Long userId)
    {
        return this.billQueryRepo
                .findBillPositionsBetweenDatesAndUserId(startDate, endDate, userId);
    }

    List<BillPositionDTO> getBillPositionsByMonthYearAndUserIdAsDto(LocalDate monthYear, long userId)
    {
        //        monthYear as starting date because of format YYYY-MM-1

        if (monthYear.getDayOfMonth() != 1)
            monthYear = LocalDate.of(monthYear.getYear(), monthYear.getMonth().getValue(), 1);

        return this.billQueryRepo
                .findBillPositionsBetweenDatesAndUserId(monthYear
                        , LocalDate.of(monthYear.getYear()
                        , monthYear.getMonth().getValue()
                        , monthYear.lengthOfMonth())
                        , userId);
    }
}
