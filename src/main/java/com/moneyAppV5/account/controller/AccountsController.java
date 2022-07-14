package com.moneyAppV5.account.controller;

import com.moneyAppV5.account.dto.AccountDTO;
import com.moneyAppV5.account.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/accounts")
class AccountsController
{
    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
    private final AccountService service;

    AccountsController(AccountService service)
    {
        this.service = service;
    }

    @GetMapping()
    String showAccounts(Model model)
    {
        model.addAttribute("account", new AccountDTO());

        return "accounts";
    }

    @PostMapping()
    String addAccount(@ModelAttribute("account") @Valid AccountDTO current, BindingResult bindingResult, Model model)
    {
        if (bindingResult.hasErrors())
        {
            model.addAttribute("message", "Błędne dane!");

            return "accounts";
        }

        if (this.service.existsByName(current.getName()))
        {
            model.addAttribute("message", "Konto o podanej nazwie już istnieje!");

            return "accounts";
        }

        this.service.createAccount(current);

        model.addAttribute("account", new AccountDTO());
        model.addAttribute("message", "Dodano konto!");

        return "accounts";
    }

    @ModelAttribute("accounts")
    List<AccountDTO> getAccountsDto()
    {
        return this.service.readAllAccountsDTO();
    }

    @ModelAttribute("allAccountsSum")
    double getAllAccountsSum()
    {
        return this.service.sumAllAccountsBalances();
    }
}
