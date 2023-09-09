package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
import com.moneyApp.account.AccountService;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/accounts")
class AccountController
{
    private final AccountService accountService;
    private final JwtService jwtService;

    public AccountController(AccountService accountService, JwtService jwtService)
    {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @PostMapping("/addAccount")
    ResponseEntity<?> createAccount(@RequestBody AccountDTO toSave, HttpServletRequest request)
    {
        var result = this.accountService.createAccountByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping("/view/{name}")
    ResponseEntity<?> getAccountByNameAndUserMail(@PathVariable String name, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.accountService.getAccountByNameAndUserEmailAsDto(name, this.jwtService.getUserEmail(request)));
    }

    @GetMapping
    ResponseEntity<?> getAccountsByUserMail(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.accountService.getAccountsByUserMailAsDto(this.jwtService.getUserEmail(request)));
    }
}
