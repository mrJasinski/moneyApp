package com.moneyApp.bill.service;

import com.moneyApp.account.service.AccountService;
import com.moneyApp.bill.Bill;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.repository.BillRepository;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.transaction.service.TransactionService;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillService
{
    private final BillRepository billRepo;
    private final UserService userService;
    private final AccountService accountService;
    private final PayeeService payeeService;
    private final BudgetService budgetService;
    private final TransactionService transactionService;

    public BillService(BillRepository billRepo, UserService userService, AccountService accountService,
                       PayeeService payeeService, BudgetService budgetService, TransactionService transactionService)
    {
        this.billRepo = billRepo;
        this.userService = userService;
        this.accountService = accountService;
        this.payeeService = payeeService;
        this.budgetService = budgetService;
        this.transactionService = transactionService;
    }

    public Bill createBillByUserEmail(BillDTO toSave, String email)
    {
        var user = this.userService.getUserByEmail(email);
        var payee = this.payeeService.getPayeeByNameAndUserId(toSave.getPayeeName(), user.getId());
        var account = this.accountService.getAccountByNameAndUserId(toSave.getAccountName(), user.getId());
        var budget = this.budgetService.getBudgetByDateAndUserId(toSave.getDate(), user.getId());
        var number = getHighestBillNumberByMonthYearAndUserId(toSave.getDate(), user.getId()) + 1;

        var bill = this.billRepo.save(new Bill(number, toSave.getDate(), payee, account, budget, toSave.getDescription(), user));

        var transactions = this.transactionService.createTransactionsByBill(toSave.getTransactions(),bill);

         var sum = transactions.stream().mapToDouble(Transaction::getAmount).sum();

        this.accountService.updateAccountBalance(account.getId(), sum, transactions.get(0).getCategory().getType());

        return bill;
    }

    long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId)
    {
        return this.billRepo.findHighestBillNumberByMonthYearAndUserId(date.getMonth().getValue(), date.getYear(), userId)
                .orElse(0L);
    }

    List<Bill> getBillsByUserId(long userId)
    {
        return this.billRepo.findByUserId(userId);
    }

    public List<BillDTO> getBillsByUserEmailAsDto(String email)
    {
        return getBillsByUserId(this.userService.getUserIdByEmail(email))
                .stream()
                .map(Bill::toDto)
                .collect(Collectors.toList());
    }
}
