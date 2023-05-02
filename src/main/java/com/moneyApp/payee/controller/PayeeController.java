package com.moneyApp.payee.controller;

import com.moneyApp.payee.PayeeRole;
import com.moneyApp.payee.dto.PayeeDTO;
import com.moneyApp.payee.service.PayeeService;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/payees")
public class PayeeController
{
    private final PayeeService payeeService;
    private final JwtService jwtService;

    public PayeeController(PayeeService payeeService, JwtService jwtService)
    {
        this.payeeService = payeeService;
        this.jwtService = jwtService;
    }

    @PostMapping
    ResponseEntity<?> createPayee(@RequestBody PayeeDTO toSave, HttpServletRequest request)
    {
        var result = this.payeeService.creatPayeeByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getPayeesByUserEmail(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.payeeService.gePayeesByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @GetMapping("/role")
    ResponseEntity<?> getPayeesByRoleAndUserMail(@RequestParam PayeeRole role, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.payeeService.getPayeesByRoleAndUserEmailAsDto(role, this.jwtService.getUserEmail(request)));
    }
}
