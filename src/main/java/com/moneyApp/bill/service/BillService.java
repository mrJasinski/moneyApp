package com.moneyApp.bill.service;

import com.moneyApp.account.service.AccountService;
import com.moneyApp.bill.Bill;
import com.moneyApp.bill.BillPosition;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.repository.BillRepository;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface BillService
{
    Bill createBillByUserEmail(BillDTO toSave, String email);

    long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId);

    List<Bill> getBillsByUserId(long userId);

    List<BillDTO> getBillsByUserEmailAsDto(String email);
}
