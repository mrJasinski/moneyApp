package com.moneyApp.bill;

import com.moneyApp.account.AccountQueryService;
import com.moneyApp.account.AccountService;
import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.dto.BillPositionDTO;
import com.moneyApp.vo.SimpleBudgetPosition;
import com.moneyApp.budget.BudgetService;
import com.moneyApp.category.CategoryService;
import com.moneyApp.payee.PayeeService;
import com.moneyApp.user.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BillService
{
    private final AccountService accountService;
    private final AccountQueryService accountQueryService;
    private final BudgetService budgetService;
    private final CategoryService categoryService;
    private final PayeeService payeeService;
    private final UserService userService;
    private final BillRepository billRepo;
    private final BillQueryService billQueryService;

    BillService(final CategoryService categoryService
            , final AccountQueryService accountQueryService
            , final PayeeService payeeService
            , final UserService userService
            , final AccountService accountService
            , final BudgetService budgetService
            , final BillRepository billRepo
            , final BillQueryService billQueryService)
    {
        this.accountService = accountService;
        this.accountQueryService = accountQueryService;
        this.budgetService = budgetService;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
        this.userService = userService;
        this.billRepo = billRepo;
        this.billQueryService = billQueryService;
    }

    public Bill createBillByUserEmail(BillDTO toSave, String email)
    {
        var user = this.userService.getUserByEmail(email);
        var payee = this.payeeService.getPayeeByNameAndUserId(toSave.getPayeeName(), user.getId());
        var account = this.accountQueryService.getSimpleAccountByNameAndUserId(toSave.getAccountName(), user.getId());
        var budget = this.budgetService.getBudgetByDateAndUserId(toSave.getDate(), user.getId());
        var number = this.billQueryService.getHighestBillNumberByMonthYearAndUserId(toSave.getDate(), user.getId()) + 1;

        var bill = this.billRepo.save(new Bill(number, toSave.getDate(), payee, account, budget, toSave.getDescription(), user));

        var transactions = createTransactionsByBill(toSave.getPositions(),bill);

        var sum = transactions.stream().mapToDouble(Bill.Position::getAmount).sum();

        this.accountService.updateAccountBalanceByAccountId(Long.parseLong(account.getId()), sum, transactions.get(0).getCategory().getType());

        return bill;
    }

    public List<Bill.Position> createTransactionsByBill(List<BillPositionDTO> transactions, Bill bill)
    {
        var result = new ArrayList<Bill.Position>();

        transactions.forEach(dto -> result.add(createBillPositionByBill(dto, bill)));

        return result;
    }

    public Bill.Position createBillPositionByBill(BillPositionDTO toSave, Bill bill)
    {
//        read from db the highest position number in given bill
//        then add 1 for current (so lowest will be 1)
        var number = this.billQueryService.getHighestBillPositionNumberByBillId(bill.getId()) + 1;

//        read bill position category by category name
        var category = this.categoryService.getCategoryByNameAndUserId(toSave.getCategoryName(), bill.getUser().getId());
        var gainer = this.payeeService.getPayeeByNameAndUserId(toSave.getGainerName(), bill.getUser().getId());

        var result = new Bill.Position(number, toSave.getAmount(), category, gainer, toSave.getDescription(), bill, bill.getUser());

        return this.billRepo.save(result);
    }

    public void updatePositionInTransaction(Bill.Position billPosition, Long budgetPositionId, long userId)
    {
        billPosition.setBudgetPosition(new SimpleBudgetPosition(String.valueOf(budgetPositionId)));
        this.billRepo.updatePositionIdInDb(billPosition.getId(), budgetPositionId, userId);
    }





}
