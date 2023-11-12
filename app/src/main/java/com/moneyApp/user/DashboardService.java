package com.moneyApp.user;

import com.moneyApp.account.AccountQueryService;
import com.moneyApp.account.AccountService;
import com.moneyApp.budget.BudgetService;
import com.moneyApp.payment.PaymentService;
import com.moneyApp.user.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService
{
    private final UserService userService;
    private final BudgetService budgetService;
    private final AccountQueryService accountQueryService;
    private final PaymentService paymentService;

    DashboardService(
            final UserService userService
            , final BudgetService budgetService
            , final AccountQueryService accountQueryService
            , final PaymentService paymentService)
    {
        this.userService = userService;
        this.budgetService = budgetService;
        this.accountQueryService = accountQueryService;
        this.paymentService = paymentService;
    }

    public Object getDashboardByUserEmailAsDto(String userEmail)
    {
        var userId = this.userService.getUserIdByEmail(userEmail);

        var userName = this.userService.getUserNameById(userId);

        var budget = this.budgetService.getBudgetByDateAndUserIdAsDto(LocalDate.now(), userId);

        var accounts = this.accountQueryService.getDashboardAccountsByUserIdAsDto(userId);

        var payments = this.paymentService.getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate.now().plusWeeks(2), userId);

        return new DashboardDTO(userName, budget, accounts, payments);
    }
}
