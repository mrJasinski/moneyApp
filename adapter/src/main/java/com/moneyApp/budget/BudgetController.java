package com.moneyApp.budget;

import com.moneyApp.budget.csv.BudgetCsvFileGenerator;
import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/budgets")
public class BudgetController
{
    private final BudgetService budgetService;
    private final JwtService jwtService;
    private final BudgetCsvFileGenerator csvGenerator;

    public BudgetController(BudgetService budgetService, JwtService jwtService, BudgetCsvFileGenerator csvGenerator)
    {
        this.budgetService = budgetService;
        this.jwtService = jwtService;
        this.csvGenerator = csvGenerator;
    }

    @PostMapping("/addBudget")
    ResponseEntity<?> createBudget(@RequestBody BudgetDTO toSave, HttpServletRequest request)
    {
        var result = this.budgetService.createBudgetByUserIdAsDto(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getMonthYear())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBudgetsByUserAsDto(HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetsByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping("/view/{number}")
    ResponseEntity<?> getBudgetByMonthAndYear(@PathVariable String number, HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetByNumberAndUserIdAsDto(number, this.jwtService.getUserIdFromToken(request)));
    }

//    edit

    @DeleteMapping("/delete")
    ResponseEntity<?> deleteBudgetByNumber(@RequestParam String number, HttpServletRequest request)
    {
        this.budgetService.deleteBudgetByNumberAndUserId(number, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.ok(String.format("Budget with number %s deleted!", number));
    }

////    TODO test
//    @GetMapping("/{number}/export")
//    void exportBudgetToCSV(@PathVariable String number, HttpServletRequest request, HttpServletResponse response) throws IOException
//    {
//        response.setContentType("text/csv");
//        response.addHeader("Content-Disposition", "attachment; filename=\"budget.csv\"");
//        this.csvGenerator.writeBudgetDtoToCsv(this.budgetService.getBudgetByNumberAndUserEmailAsDto(number,
//                this.jwtService.getUserEmail(request)), response.getWriter());
//    }
}
