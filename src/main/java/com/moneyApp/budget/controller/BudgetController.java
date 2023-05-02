package com.moneyApp.budget.controller;

import com.moneyApp.budget.dto.BudgetDTO;
import com.moneyApp.budget.service.BudgetService;
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

    public BudgetController(BudgetService budgetService, JwtService jwtService)
    {
        this.budgetService = budgetService;
        this.jwtService = jwtService;
    }

    @PostMapping
    ResponseEntity<?> createBudget(@RequestBody BudgetDTO toSave, HttpServletRequest request)
    {
        var result = this.budgetService.createBudgetByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBudgetsByUserEmail(HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetsByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @GetMapping("/{number}")
    ResponseEntity<?> getBudgetByMontAndYear(@PathVariable String number, HttpServletRequest request)
    {
        return ResponseEntity.ok().body(this.budgetService.getBudgetByNumberAndUserEmailAsDto(number, this.jwtService.getUserEmail(request)));
    }
}
