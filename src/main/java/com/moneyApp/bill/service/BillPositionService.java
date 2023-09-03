package com.moneyApp.bill.service;

import com.moneyApp.bill.Bill;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.bill.BillPosition;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.bill.repository.BillPositionRepository;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public interface BillPositionService
{
    List<BillPosition> getTransactionsByMonthYearAndUserId(LocalDate monthYear, long userId);
    List<BillPosition> createTransactionsByBill(List<BillPositionDTO> transactions, Bill bill);
    List<BillPosition> getTransactionsWithoutBudgetPositionByDateAndUserId(LocalDate startDate, LocalDate endDate, Long userId);
    List<BillPosition> getTransactionsWithoutBudgetPositionsByUserId(Long userId);
    List<BillPosition> getTransactionsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId);

    BillPosition createTransactionByBill(BillPositionDTO toSave, Bill bill);

    long getHighestTransactionNumberByBillId(long billId);

    void updatePositionInTransaction(BillPosition billPosition, BudgetPosition position, long userId);

    List<BillPositionDTO> getTransactionsByDatesAndUserMailAsDto(LocalDate startDate, LocalDate endDate, String mail);
}
