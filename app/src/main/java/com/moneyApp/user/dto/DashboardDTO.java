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
        this.shortcuts = "Konta || Bydżety || Płatności || Rachnki";
        this.budgetData = String.format("""
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
        this.paymentsData = createPaymentsData(payments);
    }

    String createPaymentsData(List<PaymentDTO> payments)
    {
        var result = new StringBuilder("Aktualne płatności: ");

        for (PaymentDTO p : payments)
            result.append(String.format("%s    %s    %s \n", p.getDescription(), p.getAmount(), p.getDate()));

        return result.toString();
    }


    public String getGreeting()
    {
        return this.greeting;
    }

    String getShortcuts()
    {
        return this.shortcuts;
    }

    public String getBudgetData()
    {
        return this.budgetData;
    }

    String getPaymentsData()
    {
        return this.paymentsData;
    }
}
