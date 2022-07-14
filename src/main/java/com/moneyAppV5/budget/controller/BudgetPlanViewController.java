package com.moneyAppV5.budget.controller;

import com.moneyAppV5.budget.dto.BudgetPositionsWrapperDTO;
import com.moneyAppV5.budget.service.BudgetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/budgetView/{hash}/plan")
class BudgetPlanViewController
{
    private final BudgetService service;

    BudgetPlanViewController(BudgetService service)
    {
        this.service = service;
    }

    @GetMapping
    String showBudgetPlan(Model model, @PathVariable Integer hash)
    {
//        TODO odczyt po hashu i wyniesienie dodatkowej logiki do serwisu?
        var result = this.service.readBudgetPlanAsDto(this.service.readBudgetByHash(hash));

        model.addAttribute("positions", this.service.readPositionsWrapperAsDto(hash));
        model.addAttribute("message", String.format("Planowanie budżetu: %s/%s", result.getMonth(), result.getYear()));
        model.addAttribute("budget", result);

        return "budgetPlan";
    }

    @PostMapping()
    String addBudgetPlan(@ModelAttribute("positions") @Valid BudgetPositionsWrapperDTO current, BindingResult bindingResult, Model model, @PathVariable Integer hash)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "budgetPlan";
        }

        var result = this.service.readBudgetPlanAsDto(this.service.readBudgetByHash(hash));

        this.service.updatePlannedAmountInPositions(current);

        model.addAttribute("positions", current);
        model.addAttribute("message", String.format("Planowanie: %s/%s", result.getMonth(), result.getYear()));
        model.addAttribute("budget", result);

        return "budgetPlan";
    }

    @ModelAttribute("budgetHash")
    int getBudgetHash(@PathVariable Integer hash)
    {
        return hash;
    }
}
