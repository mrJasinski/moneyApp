package com.moneyApp.user.service;

import com.moneyApp.account.AccountService;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.payment.service.PaymentService;
import com.moneyApp.user.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService
{
    private final UserService userService;
    private final BudgetService budgetService;
    private final AccountService accountService;
    private final PaymentService paymentService;

    DashboardService(UserService userService, BudgetService budgetService, AccountService accountService, PaymentService paymentService)
    {
        this.userService = userService;
        this.budgetService = budgetService;
        this.accountService = accountService;
        this.paymentService = paymentService;
    }

    public Object getDashboardByUserEmailAsDto(String userEmail)
    {
        var userId = this.userService.getUserIdByEmail(userEmail);

        var userName = this.userService.getUserNameById(userId);

        var budget = this.budgetService.getBudgetByDateAndUserIdAsDto(LocalDate.now(), userId);

        var accounts = this.accountService.getDashboardAccountsByUserIdAsDto(userId);

        var payments = this.paymentService.getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate.now().plusWeeks(2), userId);

        return new DashboardDTO(userName, budget, accounts, payments);
    }
}
