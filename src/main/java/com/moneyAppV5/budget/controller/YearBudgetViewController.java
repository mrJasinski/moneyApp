package com.moneyAppV5.budget.controller;

import com.moneyAppV5.budget.service.BudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/yearBudgetView/{year}")
public class YearBudgetViewController
{
    BudgetService budgetService;

    public YearBudgetViewController(BudgetService budgetService)
    {
        this.budgetService = budgetService;
    }

    @GetMapping
    String showYearBudget(Model model, @PathVariable Integer year)
    {
        model.addAttribute("budget", this.budgetService.readYearBudgetByYearAsDto(year));
//        TODO
        return "yearBudgetView";
    }
}
