package com.moneyApp.bill;

import com.moneyApp.bill.dto.BillPositionDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
interface SqlBillQueryRepository extends BillQueryRepository, JpaRepository<BillSnapshot, Long>
{
//    @Override
//    @Query(value = "SELECT MAX(b.number) FROM Bill b WHERE (EXTRACT(MONTH FROM b.billDate) = :month AND " +
//            "EXTRACT(YEAR FROM b.billDate) = :year) AND b.user.id = :userId")
//    Optional<Long> findHighestBillNumberByMonthYearAndUserId(int month, int year, long userId);
//
//    @Override
//    @Query(value = "FROM Position p WHERE (p.bill.billDate BETWEEN :startDate AND :endDate) AND p.user.id = :userId")
//    List<BillPositionDTO> findBillPositionsBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
//
//    @Override
//    @Query(value = "SELECT MAX(p.number) FROM Position p WHERE p.bill.id = :billId")
//    Optional<Long> findHighestNumberByBillId(Long billId);


//    @Override
//    @Query(value = "FROM Position p WHERE p.bill.billDate BETWEEN :startDate AND :endDate AND p.user.id = :userId AND p.budgetPosition = null")
//    List<BillPositionDTO> findBillPositionsBetweenDatesWithoutBudgetPositionByUserId(LocalDate startDate, LocalDate endDate, Long userId);
//
//    @Override
//    @Query(value = "FROM Position p WHERE p.user.id = :userId AND p.budgetPosition = null")
//    List<BillPositionDTO> findBillPositionsWithoutBudgetPositionByUserId(Long userId);

    @Override
    @Query(value = "SELECT COUNT (*) FROM BillSnapshot b WHERE b.billDate BETWEEN :startDate AND :endDate AND b.user.id = :userId")
    Integer findBillCountBetweenDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    @Override
    @Query(value = "SELECT SUM(b.amount) FROM BillPositionSnapshot b WHERE b.budgetPosition.id = :budgetPositionId")
    Double findBillPositionsSumByBudgetPositionId(Long budgetPositionId);
}
