package com.moneyApp.user;

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
    private final PaymentService paymentService;

    DashboardService(
            final UserService userService
            , final BudgetService budgetService
            , final PaymentService paymentService)
    {
        this.userService = userService;
        this.budgetService = budgetService;
        this.paymentService = paymentService;
    }

    public Object getDashboardByUserIdAsDto(long userId)
    {
        var userName = this.userService.getUserNameById(userId);

        var budget = this.budgetService.getBudgetByDateAndUserIdAsDto(LocalDate.now(), userId);

//        var payments = this.paymentService.getPaymentsFromNowTillDateWithPreviousUnpaidByUserIdAsDto(LocalDate.now().plusWeeks(2), userId);
//TODO chwilowo
//        return new DashboardDTO(userName, budget, payments);
        return new DashboardDTO(userName, budget, null);
    }
}
