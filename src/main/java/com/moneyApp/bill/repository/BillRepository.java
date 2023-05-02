package com.moneyApp.bill.repository;

import com.moneyApp.bill.Bill;
import com.moneyApp.bill.dto.BillDTO;

import java.util.List;
import java.util.Optional;

public interface BillRepository
{
    Bill save(Bill entity);

    Optional<Long> findHighestBillNumberByMonthYearAndUserId(int month, int year, long userId);

    List<Bill> findByUserId(Long userId);
}
