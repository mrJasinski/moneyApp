package com.moneyApp.user.controller;

import com.moneyApp.mail.service.MailService;
import com.moneyApp.security.JwtRequest;
import com.moneyApp.security.JwtService;
import com.moneyApp.user.dto.UserDTO;
import com.moneyApp.user.service.DashboardService;
import com.moneyApp.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class UserController
{
    private final UserService userService;
    private final DashboardService dashboardService;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Autowired
    private MailService mailService;

    @Autowired
    private SimpleMailMessage message;

    public UserController(UserService userService, DashboardService dashboardService, JwtService jwtService, AuthenticationManager authManager)
    {
        this.userService = userService;
        this.dashboardService = dashboardService;
        this.jwtService = jwtService;
        this.authManager = authManager;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest user)
    {
        var userDetails = this.authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));

        if (userDetails.isAuthenticated())
            return ResponseEntity.ok(this.jwtService.generateToken(user.getEmail()));
        else
            throw new UsernameNotFoundException("User with given credentials not found!");
    }

    @GetMapping("/myDashboard")
    public ResponseEntity<?> getUserDataAfterLogin(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.dashboardService.getDashboardByUserEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUserDetailsAfterLogin(HttpServletRequest request)
    {
        return ResponseEntity.ok(this.userService.getUserByEmailAsDto(this.jwtService.getUserEmail(request)));
    }

    @PostMapping("/register")
    ResponseEntity<?> registerUser(@RequestBody UserDTO toSave)
    {
        var user = this.userService.createUser(toSave);

        return ResponseEntity.created(URI.create("/" + user.getEmail())).body(user);
    }

//    TODO test
//    nawet zadziałało :D
    @GetMapping("/sendMail")
    ResponseEntity<?> sendMailToUser()
    {
        this.mailService.sendSimpleMessage("abakan@ymail.com", "Test", this.message.getText());

        return ResponseEntity.ok("Sent!");
    }
}
