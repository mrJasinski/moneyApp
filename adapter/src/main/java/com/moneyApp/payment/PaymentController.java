package com.moneyApp.payment;

import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("/payments")
class PaymentController
{
    private final PaymentService paymentService;
    private final JwtService jwtService;

    public PaymentController(
            PaymentService paymentService
            , JwtService jwtService)
    {
        this.paymentService = paymentService;
        this.jwtService = jwtService;
    }

    @PostMapping("/addPayment")
    ResponseEntity<?> createPayment(@RequestBody PaymentDTO toSave, HttpServletRequest request)
    {
        var result = this.paymentService.createPaymentByUserIdAsDto(toSave, this.jwtService.getUserIdFromToken(request));

        return ResponseEntity.created(URI.create("/" + result.getLinkDesc())).body(result);
    }

    @GetMapping
    ResponseEntity<?> getPaymentsByUser(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.paymentService.getPaymentsByUserIdAsDto(this.jwtService.getUserIdFromToken(request)));
    }

//    edit
//    delete
//    view


    @PutMapping("/pay")
    public ResponseEntity<?> setPaymentDateAsPaid(@RequestParam Integer hash)
    {
        this.paymentService.setPaymentPositionAsPaidByHash(hash);

        return ResponseEntity.ok(String.format("Payment #%s marked as paid!", hash));
    }
}
