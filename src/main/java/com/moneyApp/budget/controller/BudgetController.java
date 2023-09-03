package com.moneyApp.budget.controller;

import com.moneyApp.budget.CalculationType;
import com.moneyApp.budget.MonthType;
import com.moneyApp.budget.csv.BudgetCsvFileGenerator;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.service.BudgetService;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/budgets")
public class BudgetController
{
    private final BudgetService budgetService;
    private final JwtService jwtService;

    @Autowired
    private BudgetCsvFileGenerator csvGenerator;

    public BudgetController(BudgetService budgetService, JwtService jwtService)
    {
        this.budgetService = budgetService;
        this.jwtService = jwtService;
    }

    @PostMapping("/addBudget")
    ResponseEntity<?> createBudget(@RequestBody BudgetDTO toSave, HttpServletRequest request, @RequestParam MonthType monthType,
                                   @RequestParam CalculationType calculationType, @RequestParam Integer monthsCount)
    {
        var result = this.budgetService.createBudgetByUserEmail(toSave, monthType, calculationType, monthsCount , this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBudgetsByUserEmailAsDto(HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetsByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @GetMapping("/view/{number}")
    ResponseEntity<?> getBudgetByMonthAndYear(@PathVariable String number, HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetByNumberAndUserEmailAsDto(number, this.jwtService.getUserEmail(request)));
    }

//    kopiowanie budżetu
    @GetMapping("/plan/copy")
    ResponseEntity<?> getBudgetPlannedAsCopyOfMonth(@RequestParam LocalDate date, HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetPlannedAsCopyOfMonth(date, this.jwtService.getUserEmail(request)));
    }

//    TODO test
    @GetMapping("/{number}/export")
    void exportBudgetToCSV(@PathVariable String number, HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/csv");
        response.addHeader("Content-Disposition", "attachment; filename=\"budget.csv\"");
        this.csvGenerator.writeBudgetDtoToCsv(this.budgetService.getBudgetByNumberAndUserEmailAsDto(number,
                this.jwtService.getUserEmail(request)), response.getWriter());
    }
}
