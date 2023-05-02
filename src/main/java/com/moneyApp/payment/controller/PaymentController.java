package com.moneyApp.payment.controller;

import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.service.PaymentService;
import com.moneyApp.security.JwtService;
import com.moneyApp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("/payments")
public class PaymentController
{
    private final PaymentService paymentService;
    private final JwtService jwtService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, JwtService jwtService, UserService userService)
    {
        this.paymentService = paymentService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/addPayment")
    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO toSave, HttpServletRequest request)
    {
        var result = this.paymentService.createPaymentByUserEmail(toSave, this.jwtService.getUserEmail(request));

        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    public ResponseEntity<?> getPaymentsByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.paymentService.getPaymentsByUserIdAsDto(this.userService.getUserIdByEmail(this.jwtService.getUserEmail(request))));
    }
}
