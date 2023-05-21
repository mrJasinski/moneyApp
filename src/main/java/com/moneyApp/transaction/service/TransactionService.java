package com.moneyApp.transaction.service;

import com.moneyApp.bill.Bill;
import com.moneyApp.budget.BudgetPosition;
import com.moneyApp.category.service.CategoryService;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.transaction.Transaction;
import com.moneyApp.transaction.dto.TransactionDTO;
import com.moneyApp.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService
{
    private final TransactionRepository transactionRepo;
    private final CategoryService categoryService;
    private final PayeeService payeeService;

    public TransactionService(TransactionRepository transactionRepo, CategoryService categoryService, PayeeService payeeService)
    {
        this.transactionRepo = transactionRepo;
        this.categoryService = categoryService;
        this.payeeService = payeeService;
    }

    public List<Transaction> getTransactionsByMonthYearAndUserId(LocalDate monthYear, long userId)
    {
        //        monthYear jako data początkowa ponieważ jest zapisywana jako RRRR-MM-1

        if (monthYear.getDayOfMonth() != 1)
            monthYear = LocalDate.of(monthYear.getYear(), monthYear.getMonth().getValue(), 1);

        return this.transactionRepo.findTransactionsBetweenDatesAndUserId(monthYear, LocalDate.of(monthYear.getYear(),
                monthYear.getMonth().getValue(), monthYear.lengthOfMonth()), userId);
    }

    public List<Transaction> createTransactionsByBill(List<TransactionDTO> transactions, Bill bill)
    {
        var result = new ArrayList<Transaction>();

        transactions.forEach(dto -> result.add(createTransactionByBill(dto, bill)));

        return result;
    }

    Transaction createTransactionByBill(TransactionDTO toSave, Bill bill)
    {
//        wyciągnięcie z bazy najwyższego numeru transkcji w danym rachunku
//        i zwiększenie o 1 dla obecnej (aby najniższy wynosił 1)
        var number = getHighestTransactionNumberByBillId(bill.getId()) + 1;

//        odczyt kategorii transakcji po nazwie kategorii
        var category = this.categoryService.getCategoryByNameAndUserId(toSave.getCategoryName(), bill.getUser().getId());
        var gainer = this.payeeService.getPayeeByNameAndUserId(toSave.getGainerName(), bill.getUser().getId());

        var result = new Transaction(number, toSave.getAmount(), category, gainer, toSave.getDescription(), bill, bill.getUser());

       return this.transactionRepo.save(result);
    }

    long getHighestTransactionNumberByBillId(long billId)
    {
//        wyciągnięcie z bazy najwyższego numeru transkcji w danym rachunku
//        jeśli taki numer nie występuje to 0
        return this.transactionRepo.findHighestNumberByBillId(billId)
                .orElse(0L);
    }

    public void updatePositionInTransaction(Transaction transaction, BudgetPosition position, long userId)
    {
        transaction.setPosition(position);
        this.transactionRepo.updatePositionIdInDb(transaction.getId(), position, userId);
    }

    public List<Transaction> getTransactionsWithoutBudgetPositionByDateAndUserId(LocalDate startDate, LocalDate endDate, Long userId)
    {
        return this.transactionRepo.findTransactionsBetweenDatesWithoutBudgetPositionByUserId(startDate, endDate, userId);
    }

    public List<Transaction> getTransactionsWithoutBudgetPositionsByUserId(Long userId)
    {
        return this.transactionRepo.findTransactionsWithoutBudgetPositionByUserId(userId);
    }
}
