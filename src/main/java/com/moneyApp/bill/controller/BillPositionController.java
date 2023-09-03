package com.moneyApp.bill.controller;

import com.moneyApp.security.JwtService;
import com.moneyApp.bill.csv.BillPositionsCsvFileGenerator;
import com.moneyApp.bill.service.BillPositionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequestMapping("/transactions")
public class BillPositionController
{
    private final JwtService jwtService;
    private final BillPositionService billPositionService;
    private final BillPositionsCsvFileGenerator csvGenerator;

    public BillPositionController(JwtService jwtService, BillPositionService billPositionService, BillPositionsCsvFileGenerator csvGenerator)
    {
        this.jwtService = jwtService;
        this.billPositionService = billPositionService;
        this.csvGenerator = csvGenerator;
    }

    //    TODO test
    @GetMapping("/export")
    void exportTransactionsByDatesToCSV(HttpServletRequest request, HttpServletResponse response, @RequestParam LocalDate startDate,
                                        @RequestParam LocalDate endDate) throws IOException
    {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"transactions.csv\"");

        this.csvGenerator.writeTransactionsDtoToCsv(this.billPositionService.getTransactionsByDatesAndUserMailAsDto(
                startDate, endDate, this.jwtService.getUserEmail(request)), response.getWriter());
    }
}
