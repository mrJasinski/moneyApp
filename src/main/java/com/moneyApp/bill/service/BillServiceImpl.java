package com.moneyApp.bill.service;

import com.moneyApp.account.service.AccountService;
import com.moneyApp.bill.Bill;
import com.moneyApp.bill.BillPosition;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.bill.repository.BillPositionRepository;
import com.moneyApp.bill.repository.BillRepository;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.budget.service.BudgetServiceImpl;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BillServiceImpl implements BillPositionService, BillService
{
    private final AccountService accountService;
    private final BudgetServiceImpl budgetService;
    private final BillPositionRepository billPositionRepo;
    private final CategoryService categoryService;
    private final PayeeService payeeService;
    private final UserService userService;
    private final BillRepository billRepo;

    BillServiceImpl(BillPositionRepository billPositionRepo, CategoryService categoryService, PayeeService payeeService, UserService userService,
                           AccountService accountService, BudgetServiceImpl budgetService, BillRepository billRepo)
    {
        this.accountService = accountService;
        this.budgetService = budgetService;
        this.billPositionRepo = billPositionRepo;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
        this.userService = userService;
        this.billRepo = billRepo;
    }

    public Bill createBillByUserEmail(BillDTO toSave, String email)
    {
        var user = this.userService.getUserByEmail(email);
        var payee = this.payeeService.getPayeeByNameAndUserId(toSave.getPayeeName(), user.getId());
        var account = this.accountService.getAccountByNameAndUserId(toSave.getAccountName(), user.getId());
        var budget = this.budgetService.getBudgetByDateAndUserId(toSave.getDate(), user.getId());
        var number = getHighestBillNumberByMonthYearAndUserId(toSave.getDate(), user.getId()) + 1;

        var bill = this.billRepo.save(new Bill(number, toSave.getDate(), payee, account, budget, toSave.getDescription(), user));

        var transactions = createTransactionsByBill(toSave.getTransactions(),bill);

        var sum = transactions.stream().mapToDouble(BillPosition::getAmount).sum();

        this.accountService.updateAccountBalance(account.getId(), sum, transactions.get(0).getCategory().getType());

        return bill;
    }

    public long getHighestBillNumberByMonthYearAndUserId(LocalDate date, long userId)
    {
        return this.billRepo.findHighestBillNumberByMonthYearAndUserId(date.getMonth().getValue(), date.getYear(), userId)
                .orElse(0L);
    }

    public List<Bill> getBillsByUserId(long userId)
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

    public List<BillPosition> getTransactionsByMonthYearAndUserId(LocalDate monthYear, long userId)
    {
        //        monthYear jako data początkowa ponieważ jest zapisywana jako RRRR-MM-1

        if (monthYear.getDayOfMonth() != 1)
            monthYear = LocalDate.of(monthYear.getYear(), monthYear.getMonth().getValue(), 1);

        return this.billPositionRepo.findTransactionsBetweenDatesAndUserId(monthYear, LocalDate.of(monthYear.getYear(),
                monthYear.getMonth().getValue(), monthYear.lengthOfMonth()), userId);
    }

    public List<BillPosition> createTransactionsByBill(List<BillPositionDTO> transactions, Bill bill)
    {
        var result = new ArrayList<BillPosition>();

        transactions.forEach(dto -> result.add(createTransactionByBill(dto, bill)));

        return result;
    }

    public BillPosition createTransactionByBill(BillPositionDTO toSave, Bill bill)
    {
//        wyciągnięcie z bazy najwyższego numeru transkcji w danym rachunku
//        i zwiększenie o 1 dla obecnej (aby najniższy wynosił 1)
        var number = getHighestTransactionNumberByBillId(bill.getId()) + 1;

//        odczyt kategorii transakcji po nazwie kategorii
        var category = this.categoryService.getCategoryByNameAndUserId(toSave.getCategoryName(), bill.getUser().getId());
        var gainer = this.payeeService.getPayeeByNameAndUserId(toSave.getGainerName(), bill.getUser().getId());

        var result = new BillPosition(number, toSave.getAmount(), category, gainer, toSave.getDescription(), bill, bill.getUser());

        return this.billPositionRepo.save(result);
    }

    public long getHighestTransactionNumberByBillId(long billId)
    {
//        wyciągnięcie z bazy najwyższego numeru transkcji w danym rachunku
//        jeśli taki numer nie występuje to 0
        return this.billPositionRepo.findHighestNumberByBillId(billId)
                .orElse(0L);
    }

    public void updatePositionInTransaction(BillPosition billPosition, BudgetPosition position, long userId)
    {
        billPosition.setPosition(position);
        this.billPositionRepo.updatePositionIdInDb(billPosition.getId(), position, userId);
    }

    public List<BillPosition> getTransactionsWithoutBudgetPositionByDateAndUserId(LocalDate startDate, LocalDate endDate, Long userId)
    {
        return this.billPositionRepo.findTransactionsBetweenDatesWithoutBudgetPositionByUserId(startDate, endDate, userId);
    }

    public List<BillPosition> getTransactionsWithoutBudgetPositionsByUserId(Long userId)
    {
        return this.billPositionRepo.findTransactionsWithoutBudgetPositionByUserId(userId);
    }

    public List<BillPositionDTO> getTransactionsByDatesAndUserMailAsDto(LocalDate startDate, LocalDate endDate, String mail)
    {
        return getTransactionsByDatesAndUserId(startDate, endDate, this.userService.getUserIdByEmail(mail))
                .stream()
                .map(BillPosition::toDto)
                .collect(Collectors.toList());
    }

    public List<BillPosition> getTransactionsByDatesAndUserId(LocalDate startDate, LocalDate endDate, Long userId)
    {
        return this.billPositionRepo.findTransactionsBetweenDatesAndUserId(startDate, endDate, userId);
    }
}
