package com.moneyApp.user;

import com.moneyApp.budget.BudgetService;
import com.moneyApp.payment.PaymentService;
import com.moneyApp.user.dto.DashboardDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class DashboardService
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

    public DashboardDTO getDashboardByUserIdAsDto(long userId)
    {
        var userName = this.userService.getUserNameById(userId);

        var budget = this.budgetService.getBudgetByDateAndUserIdAsDto(LocalDate.now(), userId);

        var payments = this.paymentService.getUnpaidPaymentsTillDateByUserIdAsDto(LocalDate.now().plusWeeks(2), userId);
        
        return new DashboardDTO(userName, budget, payments);
    }
}
