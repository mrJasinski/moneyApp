package com.moneyApp.account;

import com.moneyApp.account.dto.AccountDTO;
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

    public AccountController(
            AccountService accountService
            , JwtService jwtService)
    {
        this.accountService = accountService;
        this.jwtService = jwtService;
    }

    @PostMapping("/addAccount")
    ResponseEntity<?> createAccount(@RequestBody AccountDTO toSave, HttpServletRequest request)
    {
        var result = this.accountService.createAccountByUserIdAsDto(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getName())).body(result);
    }

    @DeleteMapping("/deleteAccount")
    ResponseEntity<?> deleteAccount(HttpServletRequest request)
    {
//        TODO
        return ResponseEntity.ok("Account successfully deleted");
    }

    @PutMapping("/update/{name}")
    ResponseEntity<?> updateAccount(@RequestBody AccountDTO toUpdate, HttpServletRequest request)
    {
        this.accountService.updateAccountDataByUserId(toUpdate, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.ok("Account data successfully updated!");
    }

    @GetMapping("/view/{name}")
    ResponseEntity<?> getAccountByNameAndUser(@PathVariable String name, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.accountService.getAccountByNameAndUserIdAsDto(name, this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping
    ResponseEntity<?> getAccountsByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.accountService.getAccountsByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }
}
