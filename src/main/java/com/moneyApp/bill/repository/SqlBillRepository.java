package com.moneyApp.bill.repository;

import com.moneyApp.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SqlBillRepository extends BillRepository, JpaRepository<Bill, Long>
{
    @Override
    @Query(value = "SELECT MAX(b.number) FROM Bill b WHERE (EXTRACT(MONTH FROM b.date) = :month AND " +
            "EXTRACT(YEAR FROM b.date) = :year) AND b.user.id = :userId")
    Optional<Long> findHighestBillNumberByMonthYearAndUserId(int month, int year, long userId);
}
