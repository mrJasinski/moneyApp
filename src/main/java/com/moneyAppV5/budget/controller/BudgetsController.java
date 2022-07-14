package com.moneyAppV5.budget.controller;

import com.moneyAppV5.budget.dto.BudgetDTO;
import com.moneyAppV5.budget.service.BudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/budgets")
public class BudgetsController
{
    BudgetService service;

    BudgetsController(BudgetService service)
    {
        this.service = service;
    }

    @GetMapping()
    String showBudgets(Model model)
    {
        return "budgets";
    }

    @PostMapping()
    String addBudget(@ModelAttribute("budget") @Valid BudgetDTO current, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "budgets";
        }
//TODO czy to powinna być metoda w serwisie?
        if (this.service.existsByMonthAndYear(current.getMonth(), current.getYear()))
        {
            model.addAttribute("message", "Budżet na podany miesiąc już istnieje!");

            return "budgets";
        }

        var result = this.service.createBudget(current);

        this.service.createPositionsListByBudget(result);

        model.addAttribute("message", "Dodano budżet!");

        return "budgets";
    }

    @ModelAttribute("budgetsList")
    private List<BudgetDTO> getBudgetsListDto()
    {
        return this.service.readAllBudgetsDto();
    }

    @ModelAttribute("budget")
    private BudgetDTO getBudgetDto()
    {
        return new BudgetDTO(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
    }
}
