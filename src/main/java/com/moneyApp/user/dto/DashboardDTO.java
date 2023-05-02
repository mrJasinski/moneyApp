package com.moneyApp.user.dto;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.payment.dto.PaymentDTO;

import java.util.List;

public class DashboardDTO
{
    private final String greeting;
    private final String budgetData;
    private final List<AccountDTO> accounts;
    private final List<PaymentDTO> payments;

    public DashboardDTO(String userName, BudgetDTO budget, List<AccountDTO> accounts, List<PaymentDTO> payments)
    {
        this.greeting = String.format("Dzień dobry %s!", userName);
        this.budgetData = String.format("Bieżący budżet (%s/%s): planowane (%s/%s) aktualne (%s/%s)",
                budget.getMonthYear().getMonth().getValue(), budget.getMonthYear().getYear(), budget.getPlannedIncomes(),
                budget.getPlannedExpenses(), budget.getActualIncomes(), budget.getActualExpenses());
        this.accounts = accounts;
        this.payments = payments;
    }

    public String getGreeting()
    {
        return this.greeting;
    }

    public String getBudgetData()
    {
        return this.budgetData;
    }

    public List<AccountDTO> getAccounts()
    {
        return this.accounts;
    }

    public List<PaymentDTO> getPayments()
    {
        return this.payments;
    }
}
