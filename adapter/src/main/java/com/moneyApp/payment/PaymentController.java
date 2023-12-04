package com.moneyApp.payment;

import com.moneyApp.payment.dto.PaymentDTO;
import com.moneyApp.payment.PaymentService;
import com.moneyApp.security.JwtService;
import com.moneyApp.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

//    @PostMapping("/addPayment")
//    public ResponseEntity<?> createPayment(@RequestBody PaymentDTO toSave, HttpServletRequest request)
//    {
//        var result = this.paymentService.createPaymentByUserEmail(toSave, this.jwtService.getUserEmail(request));
//
//        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
//    }

//    @GetMapping
//    public ResponseEntity<?> getPaymentsByUser(HttpServletRequest request)
//    {
//        return ResponseEntity.ok(this.paymentService.getPaymentsByUserIdAsDto(this.userService.getUserIdByEmail(this.jwtService.getUserEmail(request))));
//    }

//    TODO test
//    @PostMapping("/pay")
//    public ResponseEntity<?> setPaymentDateAsPaid(@RequestParam Integer hash)
//    {
//        var paymentDate = this.paymentService.getPaymentDateByHash(hash);
//
//        this.paymentService.setPaymentDateAsPaid(paymentDate);
//
//        return ResponseEntity.ok(String.format("Payment #%s marked as paid!", paymentDate.getHash()));
//    }
}
