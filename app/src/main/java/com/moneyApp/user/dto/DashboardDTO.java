package com.moneyApp.user.dto;

import com.moneyApp.account.dto.AccountSimpleDTO;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.payment.dto.PaymentDTO;

import java.util.List;

public class DashboardDTO
{
    private final String greeting;
    private final String budgetData;
    private final List<AccountSimpleDTO> accounts;
    private final List<PaymentDTO> payments;

    public DashboardDTO(String userName, BudgetDTO budget, List<AccountSimpleDTO> accounts, List<PaymentDTO> payments)
    {
        this.greeting = String.format("Dzień dobry %s!", userName);
        this.budgetData = String.format("Bieżący budżet (%s/%s): planowane (dochody/wydatki) (%s/%s) aktualne(dochody/wydatki) (%s/%s)",
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

    public List<AccountSimpleDTO> getAccounts()
    {
        return this.accounts;
    }

    public List<PaymentDTO> getPayments()
    {
        return this.payments;
    }
}
