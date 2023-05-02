package com.moneyApp.bill.controller;

import com.moneyApp.bill.dto.BillDTO;
import com.moneyApp.bill.service.BillService;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/bills")
public class BillController
{
    private final BillService service;
    private final JwtService jwtService;

    public BillController(BillService service, JwtService jwtService)
    {
        this.service = service;
        this.jwtService = jwtService;
    }

    @PostMapping("/addBill")
    ResponseEntity<?> createBill(@RequestBody BillDTO toSave, HttpServletRequest request)
    {
        var result = this.service.createBillByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getBillsByUserEmail(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.service.getBillsByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }
}
