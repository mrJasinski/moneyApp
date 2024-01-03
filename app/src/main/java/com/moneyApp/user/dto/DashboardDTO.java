package com.moneyApp.user.dto;

import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.payment.dto.PaymentDTO;

import java.util.List;

public class DashboardDTO
{
    private final String greeting;
//    for now only string with placeholder showing concept - should contain links to given menus
    private final String shortcuts;
    private final String budgetData;
//    similar to shortcuts - for now only placeholder - should also contain timeframe (eg nearest two months)
    private final String paymentsData;

    public DashboardDTO(String userName, BudgetDTO budget, List<PaymentDTO> payments)
    {
        this.greeting = String.format("Dzień dobry %s!", userName);
        this.shortcuts = "Konta || Budżety || Płatności || Rachunki";
        this.budgetData = createBudgetData(budget);
        this.paymentsData = createPaymentsData(payments);
    }

    String createPaymentsData(List<PaymentDTO> payments)
    {
        var result = new StringBuilder("Aktualne płatności: ");

        for (PaymentDTO p : payments)
            result.append(String.format("%s    %s    %s \n", p.getDescription(), p.getAmount(), p.getDate()));

        return result.toString();
    }

    String createBudgetData(BudgetDTO budget)
    {
//        TODO
//        rather only for testing purposes
        if (budget.getMonthYear() == null)
            return """
                        Bieżący budżet (n/a):
                                    Planowane   Rzeczywiste
                         Dochody    n/a         n/a
                         Wydatki    n/a         n/a
                                    Suma        n/a
                        """;

        return String.format("""
                        Bieżący budżet (%s/%s):
                                    Planowane   Rzeczywiste
                         Dochody    %s          %s
                         Wydatki    %s          %s
                                    Suma        %s
                        """,
                budget.getMonthYear().getMonth().getValue()
                , budget.getMonthYear().getYear()
                , budget.getPlannedIncomes()
                , budget.getActualIncomes()
                , budget.getPlannedExpenses()
                , budget.getActualExpenses()
                , budget.getActualSum());
    }

    public String getGreeting()
    {
        return this.greeting;
    }

    public String getShortcuts()
    {
        return this.shortcuts;
    }

    public String getBudgetData()
    {
        return this.budgetData;
    }

    public String getPaymentsData()
    {
        return this.paymentsData;
    }
}
