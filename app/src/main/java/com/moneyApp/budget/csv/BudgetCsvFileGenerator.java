package com.moneyApp.budget.csv;

import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.dto.BudgetPositionDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;

@Component
public class BudgetCsvFileGenerator
{
    public void writeBudgetDtoToCsv(BudgetDTO budget, Writer writer)
    {
        try
        {
            var printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

//            TODO struktura wydruku
            printer.printRecord("Miesiąc:", budget.getMonthYear().getMonth(), budget.getMonthYear().getYear());
            printer.printRecord("Plan budżetu:");
            printer.printRecord(null, "Dochody:", "Wydatki:", "Suma");
            printer.printRecord("Planowane:", budget.getPlannedIncomes(), budget.getPlannedExpenses(), budget.getPlannedSum());
            printer.printRecord("Rzeczywiste:", budget.getActualIncomes(), budget.getActualExpenses(), budget.getActualSum());
            printer.printRecord("Różnica:", budget.getIncomesSum(), budget.getExpensesSum());

            printer.printRecord("Widok szczegółowy:");
            printer.printRecord("Dochody:");

            for (BudgetPositionDTO p : budget.getIncomes())
                printer.printRecord(p.getCategory().getName(), p.getPlannedAmount(), p.getActualAmount());

            printer.printRecord("Wydatki:");
            for (BudgetPositionDTO p : budget.getExpenses())
                printer.printRecord(p.getCategory().getName(), p.getPlannedAmount(), p.getActualAmount());

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
