package com.moneyApp.bill.service;

import com.moneyApp.bill.Bill;
import com.moneyApp.bill.dto.BillDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface BillService
{
    Bill createBillByUserEmail(BillDTO toSave, String email);

    long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId);

    List<Bill> getBillsByUserId(long userId);

    List<BillDTO> getBillsByUserEmailAsDto(String email);
}
