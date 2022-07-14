package com.moneyAppV5.transaction.controller;

import com.moneyAppV5.account.Account;
import com.moneyAppV5.account.service.AccountService;
import com.moneyAppV5.category.Category;
import com.moneyAppV5.category.service.CategoryService;
import com.moneyAppV5.transaction.Payee;
import com.moneyAppV5.transaction.dto.TransactionDTO;
import com.moneyAppV5.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/transactions")
class TransactionsController
{
    private static final Logger logger = LoggerFactory.getLogger(TransactionsController.class);
//    TransactionRepository repository;
    TransactionService service;
    AccountService accountService;
    CategoryService categoryService;

    TransactionsController(TransactionService service, AccountService accountService, CategoryService categoryService)
    {
        this.service = service;
        this.accountService = accountService;
        this.categoryService = categoryService;
    }

    @GetMapping()
    String showTransactions(Model model)
    {
        model.addAttribute("transaction", new TransactionDTO());
        return "transactions";
    }

//    @PostMapping()
//    String addTransaction(@ModelAttribute("transaction") @Valid TransactionDTO current, BindingResult bindingResult, Model model)
//    {
//        if (bindingResult.hasErrors())
//        {
//            model.addAttribute("message", "Błędne dane!");
//
//            return "transactions";
//        }
//
//        var transaction =  this.service.createTransaction(current);
//
//        this.accountService.changeBalance(transaction.getAccount().getId(), transaction.getAmount());
//
//        model.addAttribute("transaction", new TransactionDTO());
//        model.addAttribute("transactions", getTransactionsDto());
//        model.addAttribute("accountsList", getAccounts());
////        model.addAttribute("expensesList", getExpenseCategories());
////        model.addAttribute("incomesList", getIncomeCategories());
//        model.addAttribute("categoriesList", getCategories());
////        model.addAttribute("interialsList", getCategories());
//        model.addAttribute("payeesList", getPayees());
//        model.addAttribute("gainersList", getGainers());
//        model.addAttribute("message", "Dodano transakcję!");
////TODO czy zrobić podział transakcji na wydatki i dochody?
//        return "transactions";
//    }

//    @PostMapping(params = "addPayee")
//    String addPayee(@ModelAttribute ("transaction") PayeeDTO current)
//    {
//        this.service.createPayee(current);
//        return "transactions";
//    }

    @ModelAttribute("transactions")
    List<TransactionDTO> getTransactionsDto()
    {
        return this.service.readAllTransactionsDTO();
    }

    @ModelAttribute("accountsList")
    List<Account> getAccounts()
    {
        return this.accountService.readAllAccounts();
    }

    @ModelAttribute("expensesList")
    List<Category> getExpenseCategories()
    {
        return this.categoryService.readExpenseCategories();
    }

    @ModelAttribute("incomesList")
    List<Category> getIncomeCategories()
    {
        return this.categoryService.readIncomeCategories();
    }

    @ModelAttribute("categoriesList")
    List<Category> getCategories()
    {
//        TODO jak tu przekazać type z selecta w html żeby sortowało kategorie?
//        return this.categoryService.readCategoriesByType(type);
        return this.categoryService.readAllCategories();
    }

    @ModelAttribute("payeesList")
    List<Payee> getPayees()
    {
        return this.service.readAllPayees();
    }

//    @PostMapping
//    ResponseEntity<Transaction> createTransaction(@RequestBody @Valid Transaction toCreate) {
//        Transaction result = this.service.createTransaction(toCreate);
//
//        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
//    }
//
//    @PostMapping("/gainers")
//    ResponseEntity<Gainer> createGainer(@RequestBody @Valid Gainer toCreate) {
//        Gainer result = this.service.createGainer(toCreate);
//
//        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
//    }
//
//    @GetMapping(params = {"!sort", "!page", "!size"})
//    ResponseEntity<List<Transaction>> readAllTransactions() {
//        logger.warn("Exposing all the transactions!");
//        return ResponseEntity.ok(this.service.readAllTransactions());
//    }
//  TODO brak findById w serwisie
//    @GetMapping("/{id}")
//    ResponseEntity<Transaction> readTransaction(@PathVariable int id) {
//        return this.service.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }

//    @PutMapping("/{id}")
//    ResponseEntity<?> updateTransaction(@PathVariable int id, @RequestBody @Valid Transaction toUpdate) {
//        if (!this.service.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        this.service.readTransactionById(id)
//                .ifPresent(transaction -> {
//                    transaction.updateFrom(toUpdate);
//                    this.service.createTransaction(transaction);
//                });
//        return ResponseEntity.noContent().build();
//    }
}
