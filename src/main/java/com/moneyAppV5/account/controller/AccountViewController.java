package com.moneyAppV5.account.controller;

import com.moneyAppV5.account.service.AccountService;
import com.moneyAppV5.budget.dto.BudgetStatsWrapperDTO;
import com.moneyAppV5.budget.service.BudgetService;
import com.moneyAppV5.transaction.service.TransactionService;
import com.moneyAppV5.utils.UtilService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/accountView/{hash}")
public class AccountViewController
{
    private final AccountService service;
    private final BudgetService budgetService;
    private final TransactionService transactionService;
    private final UtilService utilService;

    public AccountViewController(AccountService service, BudgetService budgetService, TransactionService transactionService,
                                 UtilService utilService)
    {
        this.service = service;
        this.budgetService = budgetService;
        this.transactionService = transactionService;
        this.utilService = utilService;
    }

    @GetMapping()
    public String showBudgetView(Model model, @PathVariable Integer hash)
    {
        var account = this.service.readAccountByHash(hash);
        var result = this.service.readAccountAsDto(account);
//        TODO przeniesienie całości do metody serwisu "readAccountAsDto"?

        model.addAttribute("message", String.format("Konto: %s", result.getName()));
        model.addAttribute("account", result);

        model.addAttribute("actualBudgets", this.budgetService.readActualBudgetsWrapper(this.utilService.getActualMonthValue(), this.utilService.getActualYear()));

        var transactions = this.transactionService.readTransactionsByAccountId(account.getId());

        model.addAttribute("budgetStats", new BudgetStatsWrapperDTO(this.budgetService.readBudgetsWithMaxMinTransactionCountsByListAsDto(transactions),
                this.budgetService.readBudgetsWithMaxMinTransactionSumsByListAsDto(transactions)));

        return "accountView";
    }
}