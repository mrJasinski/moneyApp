package com.moneyApp.bill.csv;

import com.moneyApp.bill.dto.BillPositionDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Component
public class BillPositionsCsvFileGenerator
{
    public void writeTransactionsDtoToCsv(List<BillPositionDTO> transactions, Writer writer)
    {
        try
        {
            var printer = new CSVPrinter(writer, CSVFormat.DEFAULT);

            printer.printRecord("Data transakcji", "Kategoria", "Konto",  "Kwota", "Kontrahent", "Dla", "Opis");

            for (BillPositionDTO t : transactions)
            {
                printer.printRecord(t.getDate(), t.getCategory(), t.getAmount(), t.getPayeeName(), t.getGainerName(), t.getDescription());
            }

        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }
}
