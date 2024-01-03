package com.moneyApp.payee;

import com.moneyApp.payee.dto.PayeeDTO;
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

    @PostMapping("/addPayee")
    ResponseEntity<?> createPayee(@RequestBody PayeeDTO toSave, HttpServletRequest request)
    {
        var result = this.payeeService.createPayeeByUserIdAsDto(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getName())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getPayeesByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.payeeService.gePayeesByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

    @GetMapping("/view/{name}")
    ResponseEntity<?> getPayeeByNameAndUser(@PathVariable String name, HttpServletRequest request)
    {
        return ResponseEntity.ok(this.payeeService.getPayeeByNameAndUserIdAsDto(name, this.jwtService.getUserIdFromToken(request)));
    }

//    edit
//    delete
}
